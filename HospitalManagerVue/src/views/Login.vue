<template>
    <div class="login-index" :style="backgroundDiv">
      <div style="height: 150px;">
        <img class="login-logo" src="../assets/logo.png" alt="图片无法显示">
      </div>
        <div class="mid-index">

            <div style="
                    font-weight: 700;
                    top: 40px;
                    font-size: 28px;
                    left: 20px;
                    margin: 20px 56px;
                " class="iconfont icon-r-love">
                重庆大学医院管理系统
            </div>
            <el-form :model="loginForm" :rules="loginRules" ref="ruleForm" class="loginForm">
                <el-form-item prop="id">
                    <!--必须绑定v-model输入框才能输入字符---->
                    <el-input v-model="loginForm.id">
                        <i slot="prefix" class="iconfont icon-r-user1" style="font-size: 22px"></i>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" show-password>
                        <i slot="prefix" class="iconfont icon-r-lock" style="font-size: 22px"></i>
                    </el-input>
                </el-form-item>

                <!-- 角色单选框 -->
                <el-form-item class="role">
                    <el-radio-group v-model="role" size="small">
                        <el-radio label="患者"></el-radio>
                        <el-radio label="医生" ></el-radio>
                        <el-radio label="管理员"></el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item class="btns">
                    <el-button class="btns-left" type="primary" style="font-size: 22px; margin-left: 15px; background-color: #409ffc;" @click="submitLoginForm('ruleForm')">
                        登录</el-button>
                    <el-button class="btns-right" type="info" style="font-size: 22px; margin-left: 85px; background-color: #409ffc;" @click="registerFormVisible = true">
                        注册新账号</el-button>
                </el-form-item>
            </el-form>
        </div>

        <!-- 注册对话框 -->
        <el-dialog title="用户注册" :visible.sync="registerFormVisible">
            <el-form class="findPassword" :model="registerForm" :rules="registerRules" ref="registerForm">
                <el-form-item label="账号" label-width="80px" prop="pId">
                    <el-input v-model.number="registerForm.pId"></el-input>
                </el-form-item>
                <el-form-item label="性别" label-width="80px">
                    <el-radio v-model="registerForm.pGender" label="男">男</el-radio>
                    <el-radio v-model="registerForm.pGender" label="女">女</el-radio>
                </el-form-item>
                <el-form-item label="密码" label-width="80px" prop="pPassword">
                    <el-input v-model="registerForm.pPassword"></el-input>
                </el-form-item>
                <el-form-item label="姓名" label-width="80px" prop="pName">
                    <el-input v-model="registerForm.pName"></el-input>
                </el-form-item>
                <el-form-item label="出生日期" label-width="80px" prop="pBirthday">
                    <el-date-picker v-model="registerForm.pBirthday" type="date" placeholder="选择日期"
                        value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="手机号" label-width="80px" prop="pPhone">
                    <el-input v-model="registerForm.pPhone"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" label-width="80px" prop="pEmail">
                    <el-input v-model="registerForm.pEmail"></el-input>
                </el-form-item>
                <el-form-item label="身份证号" label-width="80px" prop="pCard">
                    <el-input v-model="registerForm.pCard"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="registerFormVisible = false" style="font-size: 18px">
                    取 消</el-button>
                <el-button type="primary" @click="registerClick('registerForm')" style="font-size: 18px">
                    确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import request from "@/utils/request.js";
import { setToken } from "@/utils/storage.js";


export default {
    name: "Login",
    data() {
        var validateMoblie = (rule, value, callback) => {/*手机号验证函数*/
            if (value === undefined) {
                callback(new Error("请输入手机号"));
            } else {
                let reg =
                    /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
                if (!reg.test(value)) {
                    callback(new Error("请输入合法的手机号"));
                }
                callback();
            }
        };

        var validateCard = (rule, value, callback) => {/*用于验证身份证号码*/
            if (value === undefined) {
                callback(new Error("请输入身份证号"));
            } else {
                let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
                if (!reg.test(value)) {
                    callback(new Error("请输入合法的身份证号码"));
                }
                callback();
            }
        };

        var validatePass = (rule, value, callback) => {/*用于验证密码是否为空，并在验证通过时重新验证“确认密码”字段*/
            if (value === "") {
                callback(new Error("请输入密码"));
            } else {
                if (this.findForm.checkPassword !== "") {
                    this.$refs.findForm.validateField("checkPassword");
                }
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {/*用于验证两次输入的密码是否一致。*/
            if (value === "") {
                callback(new Error("请再次输入密码"));
            } else if (value !== this.findForm.newPassword) {
                callback(new Error("两次输入密码不一致!"));
            } else {
                callback();
            }
        };

        return {
            //背景图片
            backgroundDiv: {
                backgroundImage:
                    "url(" + require("../assets/R-C.png") + ")",
                backgroundRepeat: "no-repeat",
                backgroundSize: "100% 100%",
            },
            loginForm: {
                id: "",
                password: "",
            },
            loginRules: {/*定义了表单验证规则*/
                id: [
                    {
                        required: true,
                        message: "请输入账号",
                        trigger: "blur",
                    },
                ],
                password: [
                    { required: true, message: "请输入密码", trigger: "blur" },
                ],
            },
            role: "患者",
            findRole: "患者",

            //找回密码
            findFormVisible: false,
            findForm: {
                code: "",
                newPassword: "",
                checkPassword: "",
                pEmail: "",
            },

            findRules: {
                pEmail: [
                    {
                        required: true,
                        message: "请输入邮箱地址",
                        trigger: "blur",
                    },
                    {
                        type: "email",
                        message: "请输入正确的邮箱地址",
                        trigger: ["blur", "change"],
                    },
                ],
                code: [
                    {
                        required: true,
                        message: "请输入验证码",
                        trigger: "blur",
                    },
                ],
                newPassword: [{ validator: validatePass, trigger: "blur" }],
                checkPassword: [{ validator: validatePass2, trigger: "blur" }],
            },
            totalTime: 60,
            content: "发送验证码",
            canClick: true,

            //注册
            registerFormVisible: false,
            registerForm: {
                pGender: "男",
            },
            registerRules: {
                pId: [
                    { required: true, message: "请输入账号", trigger: "blur" },
                    {
                        type: "number",
                        message: "账号必须数字类型",
                        trigger: "blur",
                    },
                  {
                    pattern: /^\d{8,8}$/,
                    message: "账号必须为8位数字",
                    trigger: "blur",
                  },
                ],
                pPassword: [
                    { required: true, message: "请输入密码", trigger: "blur" },
                    {
                        min: 4,
                        max: 50,
                        message: "长度在 4到 50 个字符",
                        trigger: "blur",
                    },
                ],
                pName: [
                    { required: true, message: "请输入姓名", trigger: "blur" },
                    {
                        min: 2,
                        max: 8,
                        message: "长度在 2到 8 个字符",
                        trigger: "blur",
                    },
                ],
                pEmail: [
                    { required: true, message: "请输入邮箱", trigger: "blur" },
                    {
                        type: "email",
                        message: "请输入正确的邮箱地址",
                        trigger: ["blur", "change"],
                    },
                ],
                pPhone: [{ validator: validateMoblie }],
                pCard: [{ validator: validateCard }],
                pBirthday: [
                    {
                        required: true,
                        message: "选择出生日期",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    mounted() {

    },
    methods: {
        //点击注册确认按钮
        registerClick(formName) {
            if (!/^\d+$/.test(this.registerForm.pId)) {
                this.$message.error("账号有误，只能是纯数字，且长度不能超过9位。");
                return;
            }
            if ((this.registerForm.pId + "").length > 9) {
                this.$message.error(
                    "账号不能超过9位！"
                );
                return
            }
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("patient/addPatient", {
                            params: {
                                pId: this.registerForm.pId,
                                pName: this.registerForm.pName,
                                pPassword: this.registerForm.pPassword,
                                pGender: this.registerForm.pGender,
                                pEmail: this.registerForm.pEmail,
                                pPhone: this.registerForm.pPhone,
                                pCard: this.registerForm.pCard,
                                pBirthday: this.registerForm.pBirthday,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error(
                                    "账号或邮箱已被占用！"
                                );
                            this.registerFormVisible = false;
                            this.$message.success("注册成功！");
                            console.log(res);
                        });
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
        //提交表单
        submitLoginForm(formName) {
            if (!/^\d+$/.test(this.loginForm.id)) {
                this.$message.error("账号有误，只能是纯数字");
                return;
            }
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if (this.role === "管理员") {
                        var params = new URLSearchParams();
                        params.append("aId", this.loginForm.id);
                        params.append("aPassword", this.loginForm.password);

                        request
                            .post("admin/login", params)
                            .then((res) => {
                                console.log(res);
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "用户名或密码错误"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/adminLayout");
                            })
                            .catch((e) => {
                                console.log(e);
                                if (
                                    e.response == undefined ||
                                    e.response.data == undefined
                                ) {
                                    this.$message({
                                        showClose: true,
                                        message: e,
                                        type: "error",
                                        duration: 5000,
                                    });
                                } else {
                                    this.$message({
                                        showClose: true,
                                        message: e.response.data,
                                        type: "error",
                                        duration: 5000,
                                    });
                                }
                            });
                    }
                    if (this.role === "医生") {
                        var params1 = new URLSearchParams();
                        params1.append("dId", this.loginForm.id);
                        params1.append("dPassword", this.loginForm.password);

                        request
                            .post("doctor/login", params1)
                            .then((res) => {
                                console.log(res);
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "用户名或密码错误"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/doctorLayout");
                            })
                            .catch((e) => {
                                console.log(e);
                                if (
                                    e.response == undefined ||
                                    e.response.data == undefined
                                ) {
                                    this.$message({
                                        showClose: true,
                                        message: e,
                                        type: "error",
                                        duration: 5000,
                                    });
                                } else {
                                    this.$message({
                                        showClose: true,
                                        message: e.response.data,
                                        type: "error",
                                        duration: 5000,
                                    });
                                }
                            });
                    }
                    if (this.role === "患者") {
                        var params2 = new URLSearchParams();
                        params2.append("pId", this.loginForm.id);
                        params2.append("pPassword", this.loginForm.password);

                        request
                            .post("patient/login", params2)
                            .then((res) => {
                                console.log(res);
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "用户名或密码错误"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/patientLayout");
                            })
                            .catch((e) => {
                                console.log(e);
                                if (
                                    e.response == undefined ||
                                    e.response.data == undefined
                                ) {
                                    this.$message({
                                        showClose: true,
                                        message: e,
                                        type: "error",
                                        duration: 5000,
                                    });
                                } else {
                                    this.$message({
                                        showClose: true,
                                        message: e.response.data,
                                        type: "error",
                                        duration: 5000,
                                    });
                                }
                            });
                    }
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
    }
};
</script>

<style lang="scss">
.codeInput {
    width: 70%;
    margin-right: 10px;
}

.findPassword {
    margin-top: 0px;
}

.login-index {
    background: #2b4b6b;
    height: 100%;
    position: relative;
}

.mid-index {
    box-shadow: 0 1px 10px rgba(0, 0, 0, .5), 0 1px 10px rgba(0, 0, 0, .3);
    opacity: 0.9;
    width: 450px;
    height: 360px;
    background:#f2f9ff;
    //绝对定位，相对于最左上角来说
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
}

.logo-index {
    background: white;
    height: 130px;
    width: 130px;
    border-radius: 50%;
    padding: 10px;
    //子绝父相,使一个div悬挂在另一个div上中间
    position: absolute;
    left: 50%;
    top: 0;
    transform: translate(-50%, -50%);

    border: 1px solid #eee;
    box-shadow: 0px 0px 10px #ddd;

    img {
        height: 100%;
        width: 100%;
        border-radius: 50%;
        background: #eeeeee;
    }
}


.el-form-item {
    margin-left: 20px;
    margin-right: 20px;
}

//角色单选
.role {
    margin-left: 90px;
    margin-right: 90px;
}

//按钮
.btns {
    justify-content: flex-end;
    height: 25px;
  margin: 26px 26px;
}

//自己修改
.login-logo{
  width: 100%;
  height: 100%;
  object-fit: contain;
  margin: 100px auto;
}
.btns-right:hover, .btns-left:hover{
  transform: scale(1.1);
  transition: 0.2s;
}
</style>
