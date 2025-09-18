  <template>
    <div class="page">
      <h2>Detail</h2>
      <div class="all">
        <div class="grid-container">
          <div class="grid-item"><LineChart :chartData="chartData" :threshold="50" /></div>
          <div class="grid-item"><LineChart :chartData="chartData2" :threshold="70" /></div>
          <div class="grid-item"><LineChart :chartData="chartData" :threshold="50" /></div>
          <div class="grid-item"><LineChart :chartData="chartData2" :threshold="70" /></div>
        </div> 
      </div>
    </div>
    
  </template>

  <script>
  import LineChart from '@/components/LineChart.vue'

  export default {
    name: 'Detail',
    components: { LineChart },
    data() {
      return {
        chartData: {
          labels: ["9/17", "9/18", "9/19", "9/20", "9/21", "9/22", "9/23"],
          datasets: [
            {
              label: '온도 변화',
              data: [22, 25, 20, 23, 26, 24, 27],
              borderColor: '#64CCA2',
              backgroundColor: 'rgba(100,204,162,0.2)',
              fill: true,
              tension: 0.3
            }
          ]
        },
        chartData2: {
          labels: ["9/13", "9/14", "9/15", "9/16", "9/17", "9/18", "9/19"],
          datasets: [
            {
              label: '온도 변화',
              data: [33, 35, 30, 32, 31, 29, 28],
              borderColor: '#64CCA2',
              backgroundColor: 'rgba(100,204,162,0.2)',
              fill: true,
              tension: 0.3
            }
          ]
        },
        chartData3: {
          labels: ["9/13", "9/14", "9/15", "9/16", "9/17", "9/18", "9/19"],
          datasets: [
            {
              label: '온도 변화',
              data: [33, 35, 30, 32, 31, 29, 28],
              borderColor: '#64CCA2',
              backgroundColor: 'rgba(100,204,162,0.2)',
              fill: true,
              tension: 0.3
            }
          ]
        },
        chartData4: {
          labels: ["9/13", "9/14", "9/15", "9/16", "9/17", "9/18", "9/19"],
          datasets: [
            {
              label: '온도 변화',
              data: [33, 35, 30, 32, 31, 29, 28],
              borderColor: '#64CCA2',
              backgroundColor: 'rgba(100,204,162,0.2)',
              fill: true,
              tension: 0.3
            }
          ]
        }
      }
    }
  } 
  
  </script>
<style scoped>
.page {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  margin: 0;
  padding: 0;
  border: 3px solid blue;
  overflow: hidden;
}
.all{
  height: 100%;
  border: 1px solid red;
}
.grid-container {
  flex-grow: 1;
  display: grid;
  height: 100%;
  grid-template-columns: 50% 50%;
  grid-template-rows: 50% 50% ;  /* ✅ 행 높이를 아예 50%로 고정 */
  gap: 0;                      
  padding: 0;                   
  margin: 0;                    
  width: 100%;
  border: 5px solid yellow;
}

.grid-item {
  display: flex;
  align-items: stretch;
  justify-content: stretch;
  background: #fff;
  border: 1px solid #ccc;
  box-sizing: border-box;
  border: 2px solid green;
}

.grid-item canvas {
  width: 100% !important;
  height: 100% !important; /* grid-item 크기에 맞춤 */
}
</style>
