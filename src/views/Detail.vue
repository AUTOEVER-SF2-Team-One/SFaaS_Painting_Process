    <template>
      <div class="page">
        <div class="all">
        <Sidebar :menuItems="menus" @menu-clicked="onMenuClick" />
          <div class="grid-container">
            <div class="grid-item"><LineChart :chartData="chartData" :thresholdUp="thresholdUp1" :thresholdDown="thresholdDown1" /></div>
            <div class="grid-item"><LineChart :chartData="chartData2" :thresholdUp="thresholdUp2" :thresholdDown="thresholdDown2"/></div>
            <div class="grid-item">
              <div>
                <p>🕹️ 머신 이름: {{ machineName }}</p>
                <p>🕒 측정 시간: {{ machineDate }}</p>
                <div class="status-indicator">
                <span :class="{'light-on': isMachineRun === 1, 'light-off': isMachineRun === 0}"></span>
                <span>{{ isMachineRun === 1 ? '작동 중' : '정지됨' }}</span>
              </div>
              </div>
            </div>
            <div class="grid-item">
              <DoughnutChart :chartData="doughnutData" />
            </div>
          </div> 
        </div>
      </div>
      
    </template>

  <script>
  import LineChart from '@/components/LineChart.vue'
  import Sidebar from '@/components/SideBar.vue'
  import { TrackOpTypes } from 'vue';
  import DoughnutChart from '@/components/DoughnutChart.vue';
  const DETAIL_SERVER = '/api/detail';

  ///////////////////////////api/////////////////////////////// 
  ///////////////////////////////////////////////////////////
  //console.log(response);
  export default {
      name: 'Detail',
      components: { LineChart, Sidebar, DoughnutChart },

      data() {
        return {
          totalRunTime: '',
          machines: ['error'],
          machineID: '',
          menus: [
          { label: '홈', icon: '🏠', link: '#' },
          { label: '대시보드', icon: '📊', link: '#' },
          { label: '설정', icon: '⚙️', link: '#' },
          { label: '로그아웃', icon: '🚪', link: '/logout' }
          ],
          chartData: {
          labels: [],
          datasets: []
          },
          chartData2: {
            labels: [],
            datasets: []
          },
          thresholdUp1: 0,
          thresholdDown1: 0,
          thresholdUp2: 0,
          thresholdDown2: 0,
          machineName: '',
          machineDate: '',
          machineIsRun: 0,
          doughnutData: {
            labels: ['가동', '비가동'],
            datasets: [{
              data: [0, 100], // 초기값: 0% 가동, 100% 비가동
              backgroundColor: ['#64CCA2', '#FFD166']
            }]
          },
          runRatio: 0,
          idleRatio: 0,
        }
      },
      
      methods: {
      async getDetailInformaition(machine_id) {
          function calculateTotalRunTime(dataArray) {
            if (!Array.isArray(dataArray)) {
              return {
                text: "24시간 0분",
                minutes: 1440,
              };
            }
            let totalMs = 0;

            dataArray.forEach(item => {
              if (item.start_dt && item.end_dt) {
                const start = new Date(item.start_dt);
                const end = new Date(item.end_dt);
                const diff = end - start;
                totalMs += diff;
              }
            });

            const totalMinutes = Math.floor(totalMs / 1000 / 60);
            const hours = Math.floor(totalMinutes / 60);
            const minutes = totalMinutes % 60;

            return {
              text: `${hours}시간 ${minutes}분`,
              minutes: totalMinutes
            };
          }

        try {
          const to = machine_id;
          console.log("선택된 머신 아이디:", to);
          const response = await fetch(`${DETAIL_SERVER}/information?machine_id=${to}`);
          const data = await response.json();
          const response2 = await fetch(`${DETAIL_SERVER}/machineInformation?machine_id=${to}`);
          const data2 = await response2.json();
          this.machineName = data2.machine_name + " " + to;
          this.machineDate = data2.machine_date;
          this.isMachineRun = data2.is_machine_run;
          console.log("받은 데이터:", data);
          // chartData 업데이트 로직 추가 가능
          const keys = Object.keys(data);
          console.log("데이터 키들:", keys);
          const runTime = calculateTotalRunTime(data[keys[0]]);
          this.totalRunTime = runTime.text;

          const totalMinutesInDay = 24 * 60;
          this.runRatio = Math.min(runTime.minutes, totalMinutesInDay) / 1440 * 100; // 24시간 초과 방지
          this.idleRatio = 100 - this.runRatio;
          
          console.log("가동 시간 비율:", this.runRatio, "분");
          console.log("비가동 시간 비율:", this.idleRatio, "분");
          this.doughnutData = {
            labels: ['가동', '비가동'],
            datasets: [{
              data: [this.runRatio, this.idleRatio],
              backgroundColor: ['#64CCA2', '#FFD166']
            }]
          };
          if (keys.length > 0) {
            const arr1 = data[keys[0]].slice(-5);
            console.log("첫번째 키의 최근 5개 데이터:", arr1);
            this.thresholdUp1 = arr1[0].threshold_up;
            this.thresholdDown1 = arr1[0].threshold_down;
            this.chartData = {
              labels: arr1.map(d => d.plc_create_dt.slice(11, 16)), // HH:MM
              datasets: [
                {
                  label: keys[0], // 동적으로 라벨 반영
                  data: arr1.map(d => Number(d.value)),
                  borderColor: '#64CCA2',
                  backgroundColor: 'rgba(100,204,162,0.2)',
                  fill: true,
                  tension: 0.3,
                  datalabels: {
                    display: true,
                    align: 'top',      // 점 위쪽에 표시
                    offset: 1,        // 점에서 10px 위로 오프셋
                    color: '#333',
                    font: {
                      size: 10,
                      weight: 'bold'
                    }
                  }
                }
              ]
            };
      }

      // 두 번째 key → chartData2
      if (keys.length > 1) {
        const arr2 = data[keys[1]].slice(-5);
        this.thresholdUp2 = arr2[0].threshold_up;
        this.thresholdDown2 = arr2[0].threshold_down;
        this.chartData2 = {
          labels: arr2.map(d => d.plc_create_dt.slice(11, 16)),
          datasets: [
            {
              label: keys[1],
              data: arr2.map(d => Number(d.value)),
              borderColor: '#FF6B6B',
              backgroundColor: 'rgba(255,107,107,0.2)',
              fill: true,
              tension: 0.3,
              datalabels: {
                display: true,
                align: 'top',      // 점 위쪽에 표시
                offset: 1,        // 점에서 10px 위로 오프셋
                color: '#333',
                font: {
                  size: 10,
                  weight: 'bold'
                }
              }
            }
          ]
        };
      }
        } catch (error) {
          console.error('Error during detail fetch:', error);
        }
      },
      async getMachineName(){
        try{
          const response = await fetch(`${DETAIL_SERVER}`);
          const machines = await response.json();
          console.log("받은 머신이름:", machines);
          this.machines = machines;
          const machineMenus = machines.map(m => ({
            label: m,
            icon: '🛠️',
            link: '#',
            machine_id: m.machine_id // 필요 시 식별용
          }));
          this.menus = [
            ...machineMenus, 
          ];
        }catch(error){
          console.log("에러났음 ")
          console.error('Error during machine name fetch:', error);
        }
      },
      async getMachineID(item){
        try{
              const response = await fetch(`${DETAIL_SERVER}/machineid?machine_id=${encodeURIComponent(item)}`);
              const machineId = await response.text();
              console.log("받은 머신별 데이터:", machineId);
              return machineId
          }catch(error){
            console.error('Error during machine-specific fetch:', error);
        }
      },
      async onMenuClick(item) {
        console.log("클릭된 메뉴:", item.label);

        // 홈, 대시보드, 설정, 로그아웃은 기존 로직 유지
        const machine_id = await this.getMachineID(item.label);
        this.machineID = machine_id;
        await this.getDetailInformaition(machine_id);
      }
    },
      mounted() {
        this.getMachineName(); // ✅ 페이지 진입 시 자동 호출
        this.refreshTimer = setInterval(() => {
        if (this.machineID) {
          this.getDetailInformaition(this.machineID);
        }
          }, 60000); // 60000ms = 1분
        },
        beforeUnmount() {
          // ✅ 컴포넌트 해제 시 인터벌 제거
          clearInterval(this.refreshTimer);
        }
  }
    
  </script>
  <style scoped>
  .page {
    height: 100%;
    margin-left: 200px;
    display: flex;
    flex-direction: column;
    padding: 0;
    overflow: hidden;
  }
  .all{
    height: 100%;
    grid-template-columns: 50% 50%;
  } 
  .grid-container {
    flex-grow: 1;
    display: grid;  
    height: 99.9%;
    grid-template-columns: 50% 50%;
    grid-template-rows: 50% 50% ;  /* ✅ 행 높이를 아예 50%로 고정 */
    gap: 0;                      
    padding: 0;                   
    margin: 0;                    
    width: 100%;
    /* border: 5px solid yellow; */
  }

  .grid-item {
    display: flex;
    align-items: stretch;
    justify-content: stretch;
    /* background: #fff; */
    border: 1px solid #ccc;
    box-sizing: border-box;
    border: 2px solid gray;
    border-radius: 20px;
    width: 95%;
    height: 95%;
  }

  .grid-item canvas {
    width: 90% !important;
    height: 90% !important; /* grid-item 크기에 맞춤 */  
  }
  .status-indicator {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-top: 10px;
  }

  .status-indicator .light-on,
  .status-indicator .light-off {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    display: inline-block;
    box-shadow: 0 0 5px rgba(0,0,0,0.2);
  }

  .light-on {
    background-color: #00ff00; /* 초록색 불빛 */
    box-shadow: 0 0 10px #00ff00;
  }

  .light-off {
    background-color: #555; /* 꺼진 회색 불빛 */
  }
  .grid-item:nth-child(4) {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 10px;
  }
  /* 3번째 grid-item (왼쪽 아래) */
  .grid-item:nth-child(3) {
    display: flex;  
    align-items: center;   /* 세로 가운데 */
    justify-content: center; /* 가로 가운데 */
  }
  </style>
