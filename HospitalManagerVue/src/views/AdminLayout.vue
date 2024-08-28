<template>
  <div style="padding: 10px 170px;">
    <div class="info-container">
      <div class="indexPeople">
        <div class="userImage">
          <i class="iconfont icon-r-user2" style="font-size: 132px"></i>
        </div>
        <div class="userFont">
          <div class="spanCure">
            <span>就诊概况</span>
          </div>
          <div class="spanPeople">
            <span>今天预约挂号总人数：{{ orderPeople }}</span>
          </div>
        </div>
      </div>
      <div class="indexPeople">
        <div class="userImage">
          <i class="iconfont icon-r-home" style="font-size: 132px"></i>
        </div>

        <div class="userFont">
          <div class="spanCure">
            <span>住院概况</span>
          </div>
          <div class="spanPeople">
            <span>今天住院总人数：{{ bedPeople }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 清除浮动，确保轮播图位于上方 -->
    <div style="clear: both;"></div>

    <!-- 图片轮播模块 -->
    <div class="carousel-container">
      <el-carousel height="400px" :interval="2000" type="card" arrow="always">
        <el-carousel-item v-for="(item, index) in carouselImages" :key="index">
          <img :src="item" style="width: 100%; height: 100%; object-fit: cover;" />
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 图表模块 -->
    <div class="Echarts">
      <div id="orderPeople" style="width: 1200px; height: 400px;"></div>
      <div id="orderSection" style="width: 1200px; height: 400px;"></div>
      <div id="orderGender" style="width: 600px; height: 500px; float: left;"></div>
      <div id="patientAge" style="width: 375px; height: 500px; float: right;"></div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request.js";
const images = [
  require('@/assets/img1.jpg'),
  require('@/assets/img2.jpg'),
  require('@/assets/img3.jpg')
];

export default {
  name: "AdminLayout",
  data() {
    return {
      orderPeople: 1, // 存储从服务器请求到的订单人数
      bedPeople: 1,   // 用于存储从服务器请求到的床位人数
      carouselImages: images, // 图片轮播数据
      sevenDate: [], /* 保存过去x天的日期，用于折线图的X轴数据 */
      sevenOrder: [] /* 用于保存过去x天的挂号人数数据 */
    };
  },
  methods: {
    requestPeople() {
      request.get("order/orderPeople")
          .then((res) => {
            if (res.data.status !== 200) return this.$message.error("数据请求失败");
            this.orderPeople = res.data.data;
          })
          .catch((err) => {
            console.error(err);
          });
    },

    requestBed() {
      request.get("bed/bedPeople")
          .then((res) => {
            if (res.data.status !== 200) return this.$message.error("数据请求失败");
            this.bedPeople = res.data.data;
          })
          .catch((err) => {
            console.error(err);
          });
    },

    pastSeven(num) {
      var date = new Date();
      date.setDate(date.getDate() - num);
      var time = date.getMonth() + 1 + "-" + date.getDate();
      return time;
    },

    orderPeopleChart() {
      var myChart = this.$echarts.init(document.getElementById("orderPeople"));
      request.get("order/orderSeven")
          .then((res) => {
            if (res.data.status !== 200) return this.$message.error("数据请求失败！");
            var option = {
              title: {
                text: "近20天挂号人数折线图",
                left: "5%",
              },
              xAxis: {
                type: "category",
                data: this.sevenDate,
              },
              yAxis: {
                type: "value",
              },
              series: [
                {
                  data: res.data.data,
                  type: "line",
                },
              ],
            };
            myChart.setOption(option);
            myChart.on("click", function (params) {
              const value = params.value;
              alert(`您点击的点的值是: ${value}`);
            });
          })
          .catch((err) => {
            console.error(err);
          });
    },

    orderGender() {
      var myChart = this.$echarts.init(document.getElementById("orderGender"));
      request.get("order/orderGender")
          .then(res => {
            var option = {
              title: {
                text: '患者性别比例',
                left: 'center'
              },
              tooltip: {
                trigger: 'item'
              },
              legend: {
                orient: 'vertical',
                left: 'left',
              },
              series: [
                {
                  name: '人数',
                  type: 'pie',
                  radius: '50%',
                  data: [
                    {
                      value: res.data.data.map((item) => item.countGender)[0],
                      name: res.data.data.map((item) => item.patient.pGender)[0]
                    },
                    {
                      value: res.data.data.map((item) => item.countGender)[1],
                      name: res.data.data.map((item) => item.patient.pGender)[1]
                    },

                  ],
                  emphasis: {
                    itemStyle: {
                      shadowBlur: 10,
                      shadowOffsetX: 0,
                      shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                  }
                }
              ]
            };
            myChart.setOption(option);
          })
          .catch(err => {
            console.error(err);
          });
    },

    orderSection() {
      var myChart = this.$echarts.init(document.getElementById("orderSection"));
      request.get("order/orderSection")
          .then(res => {
            var option = {
              title: {
                text: '近20天挂号科室人数统计',
                left: 'center'
              },
              xAxis: {
                type: 'category',
                data: res.data.data.map((item) => item.doctor.dSection),
                axisLabel: {
                  interval: 0,
                  rotate: 10,
                }
              },
              yAxis: {
                type: 'value'
              },
              series: [{
                data: res.data.data.map((item) => item.countSection),
                type: 'bar',
                showBackground: true,
                backgroundStyle: {
                  color: 'rgba(180, 180, 180, 0.2)'
                }
              }]
            };
            myChart.setOption(option);
          })
          .catch(err => {
            console.error(err);
          });
    },

    patientAge() {
      var myChart = this.$echarts.init(document.getElementById("patientAge"));
      request.get("patient/patientAge")
          .then(res => {
            var option = {
              title: {
                text: '患者年龄段分布',
                left: 'center'
              },
              tooltip: {
                trigger: 'item'
              },
              legend: {
                top: '5%',
                left: 'center'
              },
              series: [
                {
                  name: '年龄段',
                  type: 'pie',
                  radius: ['40%', '70%'],
                  avoidLabelOverlap: false,
                  itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 2
                  },
                  label: {
                    show: false,
                    position: 'center'
                  },
                  emphasis: {
                    label: {
                      show: true,
                      fontSize: '40',
                      fontWeight: 'bold'
                    }
                  },
                  labelLine: {
                    show: false
                  },
                  data: [
                    {value: res.data.data[0], name: '0-9岁'},
                    {value: res.data.data[1], name: '10-19岁'},
                    {value: res.data.data[2], name: '20-29岁'},
                    {value: res.data.data[3], name: '30-39岁'},
                    {value: res.data.data[4], name: '40-49岁'},
                    {value: res.data.data[5], name: '50-59岁'},
                    {value: res.data.data[6], name: '60-69岁'},
                    {value: res.data.data[7], name: '70-79岁'},
                    {value: res.data.data[8], name: '80-89岁'},
                    {value: res.data.data[9], name: '90-99岁'},
                  ]
                }
              ]
            };
            myChart.setOption(option);
          })
          .catch(err => {
            console.error(err);
          });
    },
  },
  mounted() {
    this.orderPeopleChart();
    this.orderGender();
    this.orderSection();
    this.patientAge();
  },
  created() {
    this.requestPeople();
    this.requestBed();
    //获取过去日期
    for (var i = 10; i >= -10; i--) {
      this.sevenDate.push(this.pastSeven(i));
    }
  },
};
</script>

<style lang="scss" scoped>
.userFont {
  height: 150px;
  width: 250px;
  float: right;
  color: white;

  .spanCure {
    font-size: 15px;
    margin-top: 60px;
    margin-bottom: 15px;
  }

  .spanPeople {
    font-size: 18px;
  }
}

.userImage {
  height: 150px;
  width: 150px;
  font-size: 130px;
  color: white;
  position: relative;
  left: 40px;
  top: 10px;
  float: left;
}

.indexPeople {
  height: 200px;
  width: 440px;
  background: #67a9fd;
  float: left;
  margin: 30px;
}

.carousel-container {
  margin-top: 20px;
}

.info-container {
  overflow: hidden; /* 防止子元素浮动影响父容器 */
}
</style>
