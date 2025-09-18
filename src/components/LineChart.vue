<template>
  <Line :data="chartData" :options="chartOptions" />
</template>

<script>
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title, Tooltip, Legend,
  CategoryScale, LinearScale,
  PointElement, LineElement
} from 'chart.js'
import annotationPlugin from 'chartjs-plugin-annotation'

ChartJS.register(
  Title, Tooltip, Legend,
  CategoryScale, LinearScale,
  PointElement, LineElement,
  annotationPlugin
)

export default {
  name: 'LineChart',
  components: { Line },
  props: {
    chartData: Object,
    threshold: Number
  },
  computed: {
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: 'top' },
          title: { display: true, text: '주간 온도 그래프' },
          annotation: {
            annotations: {
              thresholdLine: {
                type: 'line',
                yMin: this.threshold,
                yMax: this.threshold,
                borderColor: 'red',
                borderWidth: 2,
                label: {
                  display: true,
                  position: 'end'
                }
              }
            }
          }
        },
        scales: {
          y: {
            min: 0,
            max: 100
          }
        }
      }
    }
  }
}
</script>
