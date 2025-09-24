<template>
  <div class="page-container">
    <Sidebar :menuItems="menus" @menu-clicked="onMenuClick" />

    <div class="main-content">
      <h2 class="view-title">{{ selectedMachineName || '전체 설비' }} 다운타임 대시보드</h2>

      <div class="kpi-container">
        <div class="kpi-card">
          <h4>금일 총 다운타임</h4>
          <p>{{ kpiData.totalDowntime }} 분</p>
        </div>
        <div class="kpi-card">
          <h4>가장 빈번한 에러</h4>
          <p>{{ kpiData.mostFrequentError }}</p>
        </div>
        <div class="kpi-card">
          <h4>최장 다운타임 설비</h4>
          <p>{{ kpiData.longestDowntimeMachine }}</p>
        </div>
      </div>

      <div class="chart-container-full">
        <h3>주간 다운타임 발생 추이 (분)</h3>
        <BarChart :chartData="trendChartData" v-if="!loading" />
      </div>

      <div class="details-grid">
        <div class="log-container">
          <h3>상세 다운타임 로그</h3>
          <div class="log-table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>시작 시간</th>
                  <th>종료 시간</th>
                  <th>소요 시간(분)</th>
                  <th>에러 코드</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="log in downtimeLogs" :key="log.id">
                  <td>{{ formatDateTime(log.startTime) }}</td>
                  <td>{{ formatDateTime(log.endTime) }}</td>
                  <td>{{ log.duration }}</td>
                  <td><span class="error-code">{{ log.errorCode }}</span></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="chart-container-half">
           <h3>설비별 에러 분포</h3>
           <DoughnutChart :chartData="machineErrorData" />
        </div>
        <div class="chart-container-half">
            <h3>기종별 에러 분포</h3>
            <DoughnutChart :chartData="machineTypeErrorData" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/SideBar.vue';
import DoughnutChart from '@/components/DoughnutChart.vue';
// 주: BarChart 컴포넌트는 LineChart를 참고하여 새로 만드시거나, LineChart로 대체할 수 있습니다.
// import BarChart from '@/components/BarChart.vue'; 

const DOWNTIME_API_ENDPOINT = '/api/downtime'; // API 엔드포인트

export default {
  name: 'Downtime',
  components: { 
    Sidebar, 
    DoughnutChart,
    // BarChart 
  },
  data() {
    return {
      menus: [{ label: '전체', icon: '🌍', machine_id: 'all' }], // '전체' 메뉴 추가
      selectedMachineName: '',
      loading: true,
      
      // KPI 데이터
      kpiData: {
        totalDowntime: 0,
        mostFrequentError: 'N/A',
        longestDowntimeMachine: 'N/A',
      },
      // 추세 차트 데이터
      trendChartData: {
        labels: [], // 예: ['월', '화', '수', '목', '금', '토', '일']
        datasets: [{
          label: '다운타임(분)',
          data: [],
          backgroundColor: 'rgba(255, 107, 107, 0.5)',
        }]
      },
      // 다운타임 로그
      downtimeLogs: [],
      // 설비별 에러 차트
      machineErrorData: {
        labels: [],
        datasets: [{ data: [], backgroundColor: ['#FF6B6B', '#FFD166', '#64CCA2', '#4ECDC4', '#556270'] }]
      },
      // 기종별 에러 차트
      machineTypeErrorData: {
        labels: [],
        datasets: [{ data: [], backgroundColor: ['#C7F464', '#FF6B6B', '#4ECDC4', '#556270', '#FFD166'] }]
      },
    }
  },
  methods: {
    // 설비 목록 가져오기 (Detail.vue와 유사)
    async getMachineList() {
      try {
        // 이 부분은 Detail.vue의 getMachineName 메서드와 동일하게 구현합니다.
        // const response = await fetch('/api/detail'); 
        // const machines = await response.json();
        // this.menus.push(...machines.map(...));
        
        // --- 임시 목업 데이터 ---
        const mockMachines = [
          { machine_name: 'CNC-001', machine_id: 1 },
          { machine_name: 'PRESS-003', machine_id: 2 },
          { machine_name: 'ROBOT-A5', machine_id: 3 },
        ];
        const machineMenus = mockMachines.map(m => ({
          label: m.machine_name,
          icon: '🛠️',
          machine_id: m.machine_id
        }));
        this.menus.push(...machineMenus);
        // --- 임시 목업 데이터 끝 ---

      } catch (error) {
        console.error('Error fetching machine list:', error);
      }
    },
    
    // 메뉴 클릭 핸들러
    async onMenuClick(item) {
      this.selectedMachineName = item.machine_id === 'all' ? '' : item.label;
      await this.fetchDashboardData(item.machine_id);
    },

    // 대시보드 데이터 fetching (핵심 로직)
    async fetchDashboardData(machineId) {
      this.loading = true;
      try {
        // API 동시 요청
        const [kpiRes, trendRes, logsRes, machineErrorRes, typeErrorRes] = await Promise.all([
          fetch(`${DOWNTIME_API_ENDPOINT}/kpi?machine_id=${machineId}`),
          fetch(`${DOWNTIME_API_ENDPOINT}/trends?machine_id=${machineId}`),
          fetch(`${DOWNTIME_API_ENDPOINT}/logs?machine_id=${machineId}`),
          fetch(`${DOWNTIME_API_ENDPOINT}/errors/machine?machine_id=${machineId}`),
          fetch(`${DOWNTIME_API_ENDPOINT}/errors/type?machine_id=${machineId}`), // 기종(type)을 알기 위해 machine_id 전달
        ]);

        // KPI 데이터 업데이트
        this.kpiData = await kpiRes.json();
        
        // 추세 차트 데이터 업데이트
        const trendData = await trendRes.json();
        this.trendChartData = {
          ...this.trendChartData,
          labels: trendData.labels,
          datasets: [{ ...this.trendChartData.datasets[0], data: trendData.data }]
        };

        // 로그 데이터 업데이트
        this.downtimeLogs = await logsRes.json();

        // 도넛 차트 데이터 업데이트
        const machineError = await machineErrorRes.json();
        this.machineErrorData = {
            ...this.machineErrorData,
            labels: Object.keys(machineError),
            datasets: [{ ...this.machineErrorData.datasets[0], data: Object.values(machineError) }]
        };
        const typeError = await typeErrorRes.json();
        this.machineTypeErrorData = {
            ...this.machineTypeErrorData,
            labels: Object.keys(typeError),
            datasets: [{ ...this.machineTypeErrorData.datasets[0], data: Object.values(typeError) }]
        };

      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        this.loading = false;
      }
    },

    // 날짜/시간 포맷팅 유틸리티
    formatDateTime(isoString) {
      if (!isoString) return '';
      const date = new Date(isoString);
      return date.toLocaleString('ko-KR');
    }
  },
  async mounted() {
    await this.getMachineList();
    // 페이지 로드 시 '전체' 대시보드 데이터를 기본으로 불러옵니다.
    await this.fetchDashboardData('all');
  }
}
</script>

<style scoped>
.page-container {
  display: flex;
  height: 100%;
}

.main-content {
  flex-grow: 1;
  padding: 20px;
  margin-left: 200px; /* 사이드바 너비만큼 여백 */
  overflow-y: auto; /* 내용이 많을 경우 스크롤 */
  background-color: #f4f7f6;
}

.view-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}

/* KPI 카드 */
.kpi-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.kpi-card {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  text-align: center;
}

.kpi-card h4 {
  font-size: 1rem;
  color: #666;
  margin-bottom: 10px;
}

.kpi-card p {
  font-size: 2rem;
  font-weight: 700;
  color: #667eea;
}

/* 전체 너비 차트 컨테이너 */
.chart-container-full {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  margin-bottom: 20px;
  height: 300px; /* 차트 높이 고정 */
}

/* 상세 정보 그리드 */
.details-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr; /* 로그 테이블이 더 넓게 */
  grid-template-rows: auto;
  gap: 20px;
}

/* 로그 컨테이너 */
.log-container {
  grid-column: 1 / 2;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.log-table-wrapper {
  max-height: 400px; /* 테이블 높이 제한 및 스크롤 */
  overflow-y: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

th {
  background-color: #fafafa;
  font-weight: 600;
}

.error-code {
  background-color: #FF6B6B;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 500;
}

/* 반 너비 차트 컨테이너 */
.chart-container-half {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

h3 {
  font-size: 1.2rem;
  font-weight: 500;
  margin-bottom: 15px;
  text-align: center;
  width: 100%;
}
</style>