<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '340px'
    },
    pieData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null,
      colors: ['#FF005A', '#ffb980', '#42b098',
        '#f4516c', '#b6a2de', '#40c9c6',
        '#ffb980', '#cec5d0', '#909399',
        '#d87a80', '#ffba00', '#13ce66']
    }
  },
  watch: {
    pieData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.initChart()
    this.__resizeHandler = debounce(() => {
      if (this.chart) {
        this.chart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    setOptions(pieData) {
      const nameData = pieData.nameData
      const seriesData = pieData.seriesData
      const title = pieData.title

      this.chart.setOption({
        title: {
          text: title,
          textStyle: { fontSize: 16 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: nameData
        },
        calculable: true,
        series: [
          {
            name: title,
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '44%'],
            data: seriesData,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.pieData)
    }
  }
}
</script>
