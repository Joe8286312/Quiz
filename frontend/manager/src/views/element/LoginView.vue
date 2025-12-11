<template>
  <el-container>
    <el-header style="font-size: 40px; background-color: rgb(238, 241, 246);display: flex; justify-content: center; align-items: center;">Quiz后台管理系统登录</el-header>
    <el-main>
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="80px" class="login-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">登录</el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
import axios from "axios";

export default {
  name: 'LoginView',
  data() {
    return {
      form: {
        username: '',
        password: '',
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
      },
    };
  },
  methods: {
    onSubmit() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          console.log('提交表单:', this.form);
          // 调用后端登录接口
          this.login();
        } else {
            console.error('表单验证失败');
        }
      });
    },

    login() {
      axios.post('/admin/login', {
        username: this.form.username,
        password: this.form.password
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(res => {
        console.log('登录响应:', res.data);
        if (res.data.code === 1) {
          const token = res.data.data;
          localStorage.setItem('jwt_token', token); // 保存 token
          this.$router.push('/user'); // 登录成功跳转
        } else {
          this.$message.error('登录失败：' + res.data.message);
        }
      })
      .catch(err => {
        console.error(err);
        this.$message.error('登录异常');
      });
    }
  },
};
</script>

<style>
.login-form {
  max-width: 400px;
  margin: 50px auto;
}
</style>