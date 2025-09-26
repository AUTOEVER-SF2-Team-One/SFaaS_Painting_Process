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
      handler() {
        if (this.chartInstance) {
          this.chartInstance.destroy() // 기존 차트 제거
        }
        this.renderChart() // 새로 그리기
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
  <div class="doughnut-wrapper" style="position: relative; width: 100%; height: 100%;">
    <canvas ref="canvas"></canvas>
  </div>
</template>
