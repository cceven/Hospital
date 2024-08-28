<template>
    <el-container>
        <!-- 头部 -->
        <el-header style="height: 100px;">
            <div class="words words-left">
                <span @click="menuClick('adminLayout')">
                    <a href="http://localhost:8082" title="点击返回主界面" class="iconfont icon-r-love" style="font-size: 26px; text-decoration: none; color: black;">
                        重庆大学医院管理系统</a>
                </span>
            </div>
          <div style="height: 100%;"><img src="../assets/logo_cqu.png" alt="" style="position: relative; z-index: 1; width: 100%;height: 80%; margin: 10px auto;"></div>
            <div class="words">
                <span
                    >欢迎您，<b>{{ userName }}</b
                    >&nbsp;管理员&nbsp;</span
                >
                <span
                    ><el-button type="danger" class="logout_button" title="点击退出" @click="logout">
                        退出</el-button
                    ></span
                >
            </div>
        </el-header>

        <el-container>
            <!-- 侧边栏 -->
            <el-aside width="200px">
                <!-- 导航菜单 -->
                <el-menu
                    background-color="white"
                    text-color="grey"
                    active-text-color="black"
                    :default-active="activePath"
                >

                    <el-menu-item
                        index="adminLayout"
                        @click="menuClick('adminLayout')"
                        style="font-size: 20px"
                    >
                        首页
                    </el-menu-item>

                    <el-menu-item
                        index="doctorList"
                        @click="menuClick('doctorList')"
                        style="font-size: 20px"
                    >
                        <i
                            class="iconfont icon-r-user1"
                            style="font-size: 26px"
                        >
                        </i>
                        医生信息管理
                    </el-menu-item>

                    <el-menu-item
                        index="patientList"
                        @click="menuClick('patientList')"
                        style="font-size: 20px"
                    >
                        <i
                            class="iconfont icon-r-user2"
                            style="font-size: 26px"
                        >
                        </i>
                        患者信息管理
                    </el-menu-item>

                    <el-menu-item
                        index="orderList"
                        @click="menuClick('orderList')"
                        style="font-size: 20px"
                    >
                        <i
                            class="iconfont icon-r-paper"
                            style="font-size: 26px"
                        >
                        </i>
                        挂号信息管理
                    </el-menu-item>

                    <el-menu-item
                        index="drugList"
                        @click="menuClick('drugList')"
                        style="font-size: 20px"
                    >
                        <i class="iconfont icon-r-love" style="font-size: 26px">
                        </i>
                        药物信息管理
                    </el-menu-item>

                    <el-menu-item
                        index="checkList"
                        @click="menuClick('checkList')"
                        style="font-size: 20px"
                    >
                        <i class="iconfont icon-r-edit" style="font-size: 26px">
                        </i>
                        检查项目管理
                    </el-menu-item>

                    <el-menu-item
                        index="bedList"
                        @click="menuClick('bedList')"
                        style="font-size: 20px"
                    >
                        <i class="iconfont icon-r-list" style="font-size: 26px">
                        </i>
                        病床信息管理
                    </el-menu-item>

                    <el-menu-item
                        index="arrangeIndex"
                        @click="menuClick('arrangeIndex')"
                        style="font-size: 20px"
                    >
                        <i
                            class="iconfont icon-r-shield"
                            style="font-size: 26px"
                        >
                        </i>
                        排班信息管理
                    </el-menu-item>

<!--                    <el-menu-item
                        index="dataExpore"
                        @click="menuClick('dataExpore')"
                        style="font-size: 20px"
                    >
                        <i
                            class="iconfont icon-r-mark1"
                            style="font-size: 26px"
                        >
                        </i>
                        数据统计分析
                    </el-menu-item>-->
                </el-menu>
            </el-aside>
            <el-main>
                <!-- 子路由入口 -->
                <router-view>
                    <div>你好吗</div>
                </router-view>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import jwtDecode from "jwt-decode";

import {
    getToken,//获取存储的 token
    clearToken,//清除 token
    getActivePath,//获取当前激活的路径
    setActivePath,//设置当前激活的路径
} from "@/utils/storage.js";
export default {
    name: "Admin",
    data() {
        return {
            userName: "",//存储解码后的用户名
            activePath: "",//存储当前激活的路径
        };
    },
    methods: {
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
        //导航栏点击事件
        menuClick(path) {
            this.activePath = path;//将点击的路径 (path) 保存到 activePath 中
            setActivePath(path);//调用 setActivePath(path) 存储路径
            if (this.$route.path !== "/" + path) this.$router.push(path);//检查当前路由是否与点击的路径不同，如果不同，则使用 this.$router.push(path) 导航到点击的路径
            console.log(path);
        },
        //退出登录
        logout() {
            this.$confirm("此操作将退出登录, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {//.then() 表示用户点击“确定”按钮后要执行的操作
                    clearToken();//清除存储的 token，从而注销用户的身份信息
                    this.$message({
                        type: "success",//消息的类型是 success，表示操作成功
                        message: "退出登录成功!",
                    });
                    this.$router.push("login");//将用户重定向到登录页面
                })
                .catch(() => {//.catch() 表示用户点击“取消”按钮后要执行的操作
                    this.$message({
                        type: "info",//info，表示这是一个信息提示，而非错误或成功
                        message: "已取消",
                    });
                });
        },
    },
    mounted() {
        
    },
    created() {//一个生命周期钩子，组件创建时会执行
        //  获取激活路径
        this.activePath = getActivePath();
        // 解码token
        this.userName = this.tokenDecode(getToken()).aName;
    },
};
</script>

<style scoped lang="scss">
.title {
    cursor: pointer;
}
.el-header {
    //background-image: url("../assets/banner-bg.png");
    //background-position: center;
    background-color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .words {
        text-align: center;
        span {
            color: black;
        }
    }
  .words-left:hover{
    transform: scale(1.1);
  }
    border-bottom: 5px solid lightgrey;
}
.el-container {
    height: 100%;
}
.el-aside {
    background-color: white;
    border-right: 5px solid lightgrey;
}
.el-menu {
    border: 0;
}

.el-menu-item:hover{
  transform: scale(1.1);
}

.logout_button:hover{
  transform: scale(1.1);
}
</style>
