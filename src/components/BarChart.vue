<template>
  <Bar :data="chartData" :options="chartOptions" />
</template>

<script>
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
} from 'chart.js'
import ChartDataLabels from 'chartjs-plugin-datalabels';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, ChartDataLabels);

export default {
  name: 'BarChart',
  components: { Bar },
  props: {
    chartData: {
      type: Object,
      required: true
    }
  },
  computed: {
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false // 범례는 차트 위에 제목으로 표시되므로 숨김
          },
          datalabels: {
            anchor: 'end',
            align: 'top',
            formatter: (value) => `${value}분`,
            color: '#555',
            font: {
              weight: 'bold'
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    }
  }
}
</script>