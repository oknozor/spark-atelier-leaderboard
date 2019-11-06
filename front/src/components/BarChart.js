import React from 'react'
import ReactApexChart from 'react-apexcharts'

class BarChart extends React.Component {
  render() {

    let chart = {
      options: {
        plotOptions: {
        bar: {
          barHeight: '100%',
          distributed: true,
          horizontal: true,
          dataLabels: {
            position: 'bottom'
          },
        }
      },
      colors: ['#33b2df', '#546E7A', '#d4526e', '#13d8aa', '#A5978B', '#2b908f', '#f9a3a4', '#90ee7e',
        '#f48024', '#69d2e7'
      ],
      dataLabels: {
        enabled: true,
        textAnchor: 'start',
        style: {
          colors: ['#fff']
        },
        formatter: function (val, opt) {
          return opt.w.globals.labels[opt.dataPointIndex] + ":  " + val
        },
        offsetX: 0,
        dropShadow: {
          enabled: true
        }
      },

      stroke: {
        width: 1,
        colors: ['#fff']
      },
      xaxis: {
        categories: this.props.teams.map(team => team.name),
      },
      yaxis: {
        labels: {
          show: false
        }
      },
      title: {
          text: 'Spark contest',
          align: 'center',
          floating: true
      },
      subtitle: {
          text: 'Who will win ? You decide',
          align: 'center',
      },
      tooltip: {
        theme: 'dark',
        x: {
          show: false
        },
        y: {
          title: {
            formatter: function () {
              return ''
            }
          }
        }
      }
      },
      series: [{
        data: this.props.teams.map(team => team.score)
      }],
    }
    console.log("BarChart state :" + this.state)
    return (
      <div id="chart">
        <ReactApexChart options={chart.options} series={chart.series} type="bar" height="350" />
      </div>
    );
  }
}

export default BarChart;