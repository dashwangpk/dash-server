<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
// import { debounce } from '@/utils'

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    pieData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      height: '440px',
      width: '480px',
      fontSize: 14,
      pieFontSize: 12,
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
  activated() {
  },
  mounted() {
    this.settingSize()
    this.initChart()
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
    settingSize() {
      const heigthValue = window.screen.height * 0.56
      let widthValue = window.screen.width * 0.39
      if (window.screen.width < 1400) {
        widthValue = window.screen.width * 0.37
      }
      const value = heigthValue > widthValue ? heigthValue : widthValue
      this.width = value + 'px'
      this.height = value + 'px'

      if (value > 600) {
        this.fontSize = 20
      }
      if (value > 800) {
        this.fontSize = 24
      }
      if (value > 1000) {
        this.fontSize = 30
      }

      this.pieFontSize = this.fontSize - 2
    },
    setOptions(pieData) {
      const nameData = pieData.nameData
      const seriesData = pieData.seriesData
      const title = pieData.title
      const fontSize = this.fontSize
      const pieFontSize = this.pieFontSize

      this.chart.setOption({
        title: {
          text: title,
          textStyle: { fontSize: fontSize },
          x: 'left'
        },
        tooltip: {
          trigger: 'item',
          textStyle: { fontSize: fontSize },
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          icon: 'circle',
          // type: 'scroll',
          // orient: 'vertical',
          // right: 0,
          // top: 120,
          // top: 30,
          left: 'center',
          bottom: '20',
          itemGap: 15,
          data: nameData,
          textStyle: {
            fontSize: pieFontSize
          },
          formatter: function(name) {
            var data = seriesData
            var total = 0
            var tarValue
            for (var i = 0, l = data.length; i < l; i++) {
              total += data[i].value
              if (data[i].name === name) {
                tarValue = data[i].value
              }
            }
            var p = (tarValue / total * 100).toFixed(2)
            return name + ' ' + tarValue + ' | ' + p + '%'
          }
        },
        // calculable: true,
        series: [
          {
            name: title,
            type: 'pie',
            roseType: 'radius',
            radius: '45%',
            center: ['50%', '45%'],
            data: seriesData,
            animationEasing: 'cubicInOut',
            animationDuration: 2600,
            label: {
              normal: {
                textStyle: {
                  fontWeight: 300,
                  fontSize: fontSize // 文字的字体大小
                }
              }
            }
          }
        ]

      })
      this.chart.resize()
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.pieData)
    }
  }
}
</script>
