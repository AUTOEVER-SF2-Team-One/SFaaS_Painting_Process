<template>
  <div class="main-content">
    <h2 class="view-title">품질 현황 대시보드</h2>

    <div class="kpi-container">
      <div class="kpi-card">
        <h4>금일 총 검수량</h4>
        <p>{{ kpiData.totalInspections }} 건</p>
      </div>
      <div class="kpi-card">
        <h4>금일 불량 건수</h4>
        <p class="defect-count">{{ kpiData.totalDefects }} 건</p>
      </div>
      <div class="kpi-card">
        <h4>최근 7일 불량률</h4>
        <p>{{ kpiData.defectRate }} %</p>
      </div>
      <div class="kpi-card">
        <h4>최다 발생 불량</h4>
        <p>{{ kpiData.mostFrequentDefect }}</p>
      </div>
    </div>

    <div class="chart-grid">
      <div class="chart-container-large">
        <h3>일별 불량 발생 추이</h3>
        <BarChart :chartData="dailyTrendData" v-if="!loading" />
      </div>
      <div class="chart-container-small">
         <h3>불량 유형별 분포</h3>
         <DoughnutChart :chartData="defectTypeData" />
      </div>
      <div class="chart-container-small">
          <h3>설비별 불량 발생 건수</h3>
          <DoughnutChart :chartData="defectByMachineData" />
      </div>
    </div>
    
    <div class="log-container">
      <h3>상세 불량 내역</h3>
      <div class="log-table-wrapper">
        <table>
          <thead>
            <tr>
              <th>검수일시</th>
              <th>차대번호(VIN)</th>
              <th>색상</th>
              <th>불량 유형</th>
              <th>발생 설비</th>
              <th>검수 담당자</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="log in defectLogs" :key="log.id">
              <td>{{ formatDateTime(log.inspectionTime) }}</td>
              <td>{{ log.vin }}</td>
              <td>{{ log.color }}</td>
              <td><span class="error-tag">{{ log.errorName }}</span></td>
              <td>{{ log.machineName }}</td>
              <td>{{ log.inspectorName }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
// Sidebar 임포트 제거
import DoughnutChart from '@/components/DoughnutChart.vue';
import BarChart from '@/components/BarChart.vue';

const DEFECT_API = '/api/defect';

export default {
  name: 'Defect',
  // Sidebar 컴포넌트 등록 제거
  components: { 
    DoughnutChart,
    BarChart 
  },
  data() {
    return {
      // menus 데이터 제거
      loading: true,
      kpiData: { totalInspections: 0, totalDefects: 0, defectRate: 0.0, mostFrequentDefect: 'N/A' },
      dailyTrendData: {
        labels: [],
        datasets: [{ label: '불량 건수', data: [], backgroundColor: 'rgba(255, 99, 132, 0.5)' }]
      },
      defectTypeData: { 
        labels: [], 
        datasets: [{ data: [], backgroundColor: ['#FF6B6B', '#FFD166', '#64CCA2', '#4ECDC4', '#556270'] }] 
      },
      defectByMachineData: { 
        labels: [], 
        datasets: [{ data: [], backgroundColor: ['#C7F464', '#FF6B6B', '#4ECDC4', '#556270', '#FFD166'] }] 
      },
      defectLogs: [],
    }
  },
  methods: {
    // onMenuClick 메소드 제거
    async fetchDashboardData() {
      this.loading = true;
      try {
        // --- 임시 목(Mock) 데이터 ---
        this.kpiData = { totalInspections: 152, totalDefects: 12, defectRate: 7.9, mostFrequentDefect: '오렌지 필' };
        
        const trendData = { labels: ['09-18', '09-19', '09-20', '09-21', '09-22', '09-23', '09-24'], data: [2, 3, 1, 2, 1, 2, 1] };
        const typeData = { '오렌지 필': 5, '도장 흘러내림': 3, '핀홀': 2, '이물질 부착': 2 };
        const machineData = { '2번 도장 로봇': 7, '1번 도장 로봇': 4, '1번 건조 오븐': 1 };
        this.defectLogs = [
            { id: 1, inspectionTime: '2025-09-24 11:35:20', vin: 'KNA12345VIN005', color: 'Mars Orange', errorName: '도장 흘러내림', machineName: '2번 도장 로봇', inspectorName: '이영희' },
            { id: 2, inspectionTime: '2025-09-23 16:55:00', vin: 'KNA12345VIN007', color: 'Steel Gray', errorName: '오렌지 필', machineName: '1번 도장 로봇', inspectorName: '김철수' },
            { id: 3, inspectionTime: '2025-09-23 14:10:30', vin: 'KNA12345VIN006', color: 'Snow White Pearl', errorName: '핀홀', machineName: '1번 건조 오븐', inspectorName: '김철수' },
            { id: 4, inspectionTime: '2025-09-22 09:15:45', vin: 'KNA12345VIN008', color: 'Aurora Black', errorName: '오렌지 필', machineName: '2번 도장 로봇', inspectorName: '이영희' },
        ];
        // --- 임시 목(Mock) 데이터 끝 ---

        this.dailyTrendData = {
          ...this.dailyTrendData,
          labels: trendData.labels,
          datasets: [{ ...this.dailyTrendData.datasets[0], data: trendData.data }]
        };

        this.defectTypeData = {
            labels: Object.keys(typeData),
            datasets: [{ ...this.defectTypeData.datasets[0], data: Object.values(typeData) }]
        };

        this.defectByMachineData = {
            labels: Object.keys(machineData),
            datasets: [{ ...this.defectByMachineData.datasets[0], data: Object.values(machineData) }]
        };

      } catch (error) {
        console.error('Failed to fetch defect dashboard data:', error);
      } finally {
        this.loading = false;
      }
    },

    formatDateTime(isoString) {
      if (!isoString) return '-';
      const date = new Date(isoString);
      return date.toLocaleString('ko-KR', { timeZone: 'Asia/Seoul' });
    }
  },
  mounted() {
    this.fetchDashboardData();
  }
}
</script>

<style scoped>
/* page-container 스타일 제거 */
.main-content {
  flex-grow: 1;
  /* margin-left 제거하여 전체 너비 사용 */
  background-color: #f4f7f6;
  width: 100%; /* 전체 너비 차지하도록 설정 */
}
.view-title {
  font-size: 2rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 25px;
}
.kpi-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
  margin-bottom: 25px;
}
.kpi-card {
  background: #fff;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  text-align: center;
  border-top: 4px solid #667eea;
}
.kpi-card h4 {
  font-size: 1.1rem;
  color: #555;
  margin-bottom: 12px;
}
.kpi-card p {
  font-size: 2.2rem;
  font-weight: 700;
  color: #333;
}
.kpi-card .defect-count {
  color: #FF6B6B;
}
.chart-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  gap: 20px;
  margin-bottom: 25px;
}
.chart-container-large {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  height: 350px;
}
.chart-container-small {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  height: 350px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.log-container {
  background: #fff;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}
.log-table-wrapper {
  max-height: 450px;
  overflow-y: auto;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  padding: 14px 18px;
  text-align: left;
  border-bottom: 1px solid #eee;
}
th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
}
.error-tag {
  background-color: #FF6B6B;
  color: white;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 0.85rem;
  font-weight: 500;
}
h3 {
  font-size: 1.4rem;
  font-weight: 600;
  margin-bottom: 20px;
  text-align: center;
  width: 100%;
  color: #2c3e50;
}
</style>