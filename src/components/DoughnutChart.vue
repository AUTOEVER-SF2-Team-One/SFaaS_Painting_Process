<script>
import { Chart, Title, Tooltip, Legend, ArcElement, DoughnutController } from 'chart.js'
import ChartDataLabels from 'chartjs-plugin-datalabels'

Chart.register(Title, Tooltip, Legend, ArcElement, DoughnutController, ChartDataLabels)

export default {
  name: 'DoughnutChart',
  props: {
    chartData: Object
  },
  data() {
    return {
      chartInstance: null
    }
  },
  mounted() {
    this.renderChart()
  },
  watch: {
    chartData: {
      deep: true,
      handler(newData) {
        if (this.chartInstance) {
          // 새 데이터로 차트 업데이트
          this.chartInstance.data = newData;
          this.chartInstance.update(); // 차트 다시 그리기
        } else {
          // 차트 인스턴스가 없으면 새로 렌더링
          this.renderChart();
        }
      }
    }
  },
  methods: {
    renderChart() {
      this.chartInstance = new Chart(this.$refs.canvas, {
        type: 'doughnut',
        data: this.chartData,
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { position: 'bottom' },
            datalabels: {
              color: '#000',
              formatter: (value) => `${value.toFixed(1)}%`
            }
          }
        }
      })
    }
  }
}
</script>

<template>
  <div>
    <canvas ref="canvas"></canvas>
  </div>
</template>
