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
    thresholdUp: Number,
    thresholdDown: Number
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
              thresholdUpLine: {
                type: 'line',
                yMin: this.thresholdUp,
                yMax: this.thresholdUp,
                borderColor: 'red',
                borderWidth: 2,
                label: {
                  display: true,
                  position: 'end'
                }
              },
              thresholdDownLine: {
                type: 'line',
                yMin: this.thresholdDown,
                yMax: this.thresholdDown,
                borderColor: 'blue',
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
            max: 200
          }
        }
      }
    }
  }
}
</script>
