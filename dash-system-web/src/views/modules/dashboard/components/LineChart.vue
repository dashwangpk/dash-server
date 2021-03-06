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
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      colors: ['#FF005A', '#ffb980', '#42b098',
        '#f4516c', '#b6a2de', '#40c9c6',
        '#ffb980', '#cec5d0', '#909399',
        '#d87a80', '#ffba00', '#13ce66'],
      chart: null,
      sidebarElm: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.initChart()
      }
    }
  },
  mounted() {
    this.initChart()
    if (this.autoResize) {
      this.__resizeHandler = debounce(() => {
        if (this.chart) {
          this.chart.resize()
        }
      }, 100)
      window.addEventListener('resize', this.__resizeHandler)
    }

    // 监听侧边栏的变化
    this.sidebarElm = document.getElementsByClassName('sidebar-container')[0]
    this.sidebarElm && this.sidebarElm.addEventListener('transitionend', this.sidebarResizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    if (this.autoResize) {
      window.removeEventListener('resize', this.__resizeHandler)
    }

    this.sidebarElm && this.sidebarElm.removeEventListener('transitionend', this.sidebarResizeHandler)

    this.chart.dispose()
    this.chart = null
  },
  methods: {
    sidebarResizeHandler(e) {
      if (e.propertyName === 'width') {
        this.__resizeHandler()
      }
    },
    setOptions(lineChartData) {
      const title = lineChartData.title
      const tempSeriesDate = {
        name: '',
        smooth: true,
        type: 'line',
        itemStyle: {
          normal: {
            color: '#FF005A',
            lineStyle: {
              color: '#FF005A',
              width: 2
            }
          }
        },
        data: [],
        animationDuration: 2800,
        animationEasing: 'quadraticOut'
      }
      const series = []
      for (var i = 0; i < lineChartData.nameData.length; i++) {
        const seriesDate = { ...tempSeriesDate }
        seriesDate.itemStyle = {
          normal: {
            color: '#FF005A',
            lineStyle: {
              color: '#FF005A',
              width: 2
            }
          }
        }

        seriesDate.data = lineChartData.chartData[lineChartData.nameData[i]]
        seriesDate.name = lineChartData.nameData[i]
        seriesDate.itemStyle.normal.color = this.colors[i % 12]
        seriesDate.itemStyle.normal.lineStyle.color = this.colors[i % 12]
        series.push(seriesDate)
      }

      this.chart.setOption({
        title: {
          text: title,
          textStyle: { fontSize: 16 }
        },
        grid: {
          left: 10,
          right: 36,
          bottom: 20,
          top: 36,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        xAxis: {
          data: lineChartData.rangeDate,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: {
          data: lineChartData.nameData
        },
        series: series
      }, true)
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    }
  }
}
</script>
