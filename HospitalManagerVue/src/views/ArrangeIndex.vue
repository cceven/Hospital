<template>
    <div>
        <el-card>
            <div>
                <div id="arrangeIndex" style="font-size: 18px;">
                    请选择值班日期：
                </div>
                <br />
                <ul class="dateUl">
                    <li v-for="monthDay in monthDays" :key="monthDay">
                        <el-button
                            class="date-button"
                            type="primary"
                            style="margin: 5px;font-size: 18px;"
                            @click="dateClick(monthDay)"
                        >
                            {{ monthDay }}</el-button
                        >
                    </li>
                </ul>
            </div>
            <div class="router-view">
                <router-view></router-view>
            </div>
        </el-card>
    </div>
</template>

<script>
import { getActivePath, setActivePath } from "@/utils/storage.js";

const ARRANGEDATE = "arrangeDate";
export default {
    name: "ArrangeIndex",
    data() {
        return {
            monthDays: [],/*存储当天及未来几天的日期数组*/
            monthDay: "",/*存储当前选中的日期*/
            activePath: "",
        };
    },

    methods: {
        //日历点击
        dateClick(monthDay) {
            console.log(monthDay);

            const nowDate = new Date();
            let year = nowDate.getFullYear();/*获取当前年份 year*/
            let dateTime = year + "-" + monthDay;/*将年份和点击的日期组合成完整的日期字符串 dateTime（格式为 "YYYY-MM-DD"）*/
            sessionStorage.setItem(ARRANGEDATE, dateTime);

            this.activePath = "sectionIndex";/*科室*/
            setActivePath("sectionIndex");
            if (this.$route.path !== "/sectionIndex")
                this.$router.push("sectionIndex");
        },

        //获取当天及后30天的日期星期
        nowDay(num) {
            var nowDate = new Date();
            nowDate.setDate(nowDate.getDate() + num);/*num通过for循环传递*/
            var month = nowDate.getMonth() + 1;/*基于0的索引，要加1*/
            var date = nowDate.getDate();
            /*如果月或日小于10，则在前面补 0*/
            if (date < 10) {
                date = "0" + date;
            }
            if (month < 10) {
                month = "0" + month;
            }
            var time = month + "-" + date;
            this.monthDays.push(time);
        },
    },
    mounted() {
        
    },
    created() {
        //获取当天的后30天
        for (var i = 0; i < 30; i++) {
            this.nowDay(i);
            //  获取激活路径
            this.activePath = getActivePath();/*确保在每次日期变化时，组件的 activePath 属性总是与存储中的路径同步*/
        }
    },
};
</script>

<style scoped lang="scss">
.disabled {
    background-color: #ddd;
    border-color: #ddd;
    color: black;
    cursor: not-allowed; // 鼠标变化
    pointer-events: none;
}
.router-view {
    margin-top: 20px;
}
.sectionUl li {
    display: inline;
    padding: 60px;
}
.dateUl li {
    display: inline;
    //margin: 5px;
    padding: 1px;
}
.date-button:hover{
  transform: scale(1.1);
}
</style>