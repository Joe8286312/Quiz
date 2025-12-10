<template>
  <el-container style="height: 100vh;">
    <el-header style="font-size: 40px; background-color: rgb(238, 241, 246)">
      Quiz后台管理

      <!-- 右侧退出按钮 -->
      <div style="position:absolute; right:20px; top:-1px;">
        <el-button type="danger" size="mini" @click="logout">退出</el-button>
      </div>
    </el-header>

    <el-container style="flex: 1; border: 1px solid #eee">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <el-menu :default-openeds="['1']" style="height: 100%;">
          <el-submenu index="1">
            <template slot="title">管理选项</template>
            <el-menu-item-group>
              <el-menu-item index="1-1">
                <router-link to="/user" class="no-underline">用户管理</router-link>
              </el-menu-item>
              <el-menu-item index="1-2">
                <router-link to="/question" class="no-underline">题目管理</router-link>
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-main style="padding: 20px; overflow: auto;">
        <!-- 顶部查询的表单 -->
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item label="用户名">
            <el-input
              v-model="formInline.keyword"
              placeholder="请输入用户名关键词"
              @keyup.enter.native="onSearch"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSearch">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button @click="resetSearch" plain>重置</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="addNewUser">添加用户</el-button>
          </el-form-item>
        </el-form>

        <!-- 显示的table -->
        <el-table :data="tableData" style="width: 100%; margin-top: 20px;" v-loading="loading">
          <el-table-column label="ID" width="80">
            <template slot-scope="scope">
              <span>{{ scope.row.id }}</span>
            </template>
          </el-table-column>
          <el-table-column label="用户名" width="180">
            <template slot-scope="scope">
              <span>{{ scope.row.userName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="用户角色" width="120">
            <template slot-scope="scope">
              <el-tag :type="scope.row.userRole === 1 ? 'danger' : 'success'" size="small">
                {{ scope.row.userRole === 1 ? '管理员' : '普通用户' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" width="180">
            <template slot-scope="scope">
              <span>{{ formatDate(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="更新时间" width="180">
            <template slot-scope="scope">
              <span>{{ formatDate(scope.row.updateTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <br />
        
        <!-- 分页 -->
        <el-pagination 
          background 
          layout="prev, pager, next, total"
          :page-size="pageSize"
          :total="total"
          :current-page="currentPage"
          @current-change="handlePageChange"
          :hide-on-single-page="total <= pageSize"
        >
        </el-pagination>

        <!-- 添加用户对话框 -->
        <el-dialog 
          title="添加用户" 
          :visible.sync="dialogFormVisible"
          :close-on-click-modal="false"
          @closed="resetForm"
        >
          <el-form :model="form" :rules="formRules" ref="formRef">
            <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
              <el-input v-model="form.username" autocomplete="off" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
              <el-input v-model="form.password" type="password" autocomplete="new-password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" :label-width="formLabelWidth" prop="checkpassword">
              <el-input v-model="form.checkpassword" type="password" autocomplete="new-password" placeholder="请确认密码"></el-input>
            </el-form-item>
            <el-form-item label="用户角色" :label-width="formLabelWidth" prop="userRole">
              <el-select v-model="form.userRole" placeholder="请选择用户角色" style="width: 100%">
                <el-option label="普通用户" :value="0"></el-option>
                <el-option label="管理员" :value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="onAddNewUser" :loading="adding">确 定</el-button>
          </div>
        </el-dialog>

        <!-- 编辑用户对话框 -->
        <el-dialog 
          title="编辑用户" 
          :visible.sync="editDialogFormVisible"
          :close-on-click-modal="false"
          @closed="resetEditForm"
        >
          <el-form :model="editForm" :rules="editFormRules" ref="editFormRef">
            <el-form-item label="ID" :label-width="formLabelWidth">
              <el-input v-model="editForm.id" disabled></el-input>
            </el-form-item>
            <el-form-item label="用户名" :label-width="formLabelWidth" prop="userName">
              <el-input v-model="editForm.userName" autocomplete="off" placeholder="请输入用户名"></el-input>
            </el-form-item>
            
            <!-- 🔴 修改：使用复选框控制是否修改密码 -->
            <el-form-item label="修改密码" :label-width="formLabelWidth">
              <el-switch
                v-model="editForm.changePassword"
                active-text="修改密码"
                inactive-text="保持原密码"
                @change="handlePasswordChangeSwitch"
              ></el-switch>
            </el-form-item>
            
            <!-- 🔴 修改：密码输入框根据开关状态显示/隐藏 -->
            <el-form-item 
              v-if="editForm.changePassword" 
              label="新密码" 
              :label-width="formLabelWidth" 
              prop="newPassword"
            >
              <el-input 
                v-model="editForm.newPassword" 
                type="password" 
                autocomplete="new-password" 
                placeholder="请输入新密码"
              ></el-input>
            </el-form-item>
            
            <el-form-item 
              v-if="editForm.changePassword" 
              label="确认新密码" 
              :label-width="formLabelWidth" 
              prop="confirmPassword"
            >
              <el-input 
                v-model="editForm.confirmPassword" 
                type="password" 
                autocomplete="new-password" 
                placeholder="请确认新密码"
              ></el-input>
            </el-form-item>
            
            <el-form-item label="用户角色" :label-width="formLabelWidth" prop="userRole">
              <el-select v-model="editForm.userRole" placeholder="请选择用户角色" style="width: 100%">
                <el-option label="普通用户" :value="0"></el-option>
                <el-option label="管理员" :value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="editDialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="onEditUser" :loading="editing">确 定</el-button>
          </div>
        </el-dialog>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    // 密码验证规则
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else if (value.length < 6) {
        callback(new Error('密码长度不能少于6位'));
      } else {
        callback();
      }
    };
    
    const validateCheckPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'));
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致'));
      } else {
        callback();
      }
    };
    
    // 🔴 新增：编辑时的新密码验证
    const validateNewPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'));
      } else if (value.length < 6) {
        callback(new Error('新密码长度不能少于6位'));
      } else {
        callback();
      }
    };
    
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认新密码'));
      } else if (value !== this.editForm.newPassword) {
        callback(new Error('两次输入的新密码不一致'));
      } else {
        callback();
      }
    };

    return {
      dialogFormVisible: false,
      editDialogFormVisible: false,
      loading: false,
      adding: false,
      editing: false,
      currentPage: 1,
      pageSize: 5,
      total: 0,
      searchMode: false,
      currentKeyword: '',
      formInline: {
        keyword: "",
      },
      tableData: [],
      form: {
        username: "",
        password: "",
        checkpassword: "",
        userRole: 0,
      },
      // 🔴 修改：编辑表单数据结构
      editForm: {
        id: null,
        userName: "",
        changePassword: false, // 是否修改密码的开关
        newPassword: "",       // 新密码
        confirmPassword: "",   // 确认新密码
        userRole: 0,
      },
      formLabelWidth: "120px",
      formRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, validator: validatePassword, trigger: 'blur' }
        ],
        checkpassword: [
          { required: true, validator: validateCheckPassword, trigger: 'blur' }
        ],
        userRole: [
          { required: true, message: '请选择用户角色', trigger: 'change' }
        ]
      },
      // 🔴 修改：编辑表单验证规则
      editFormRules: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        newPassword: [
          { validator: validateNewPassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { validator: validateConfirmPassword, trigger: 'blur' }
        ],
        userRole: [
          { required: true, message: '请选择用户角色', trigger: 'change' }
        ]
      }
    };
  },
  methods: {
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '';
      try {
        const date = new Date(dateString);
        return date.toLocaleString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        });
      } catch (e) {
        return dateString;
      }
    },
    
    addNewUser() {
      this.dialogFormVisible = true;
    },
    
    // 重置搜索
    resetSearch() {
      this.formInline.keyword = '';
      this.currentKeyword = '';
      this.searchMode = false;
      this.currentPage = 1;
      this.loadData();
    },
    
    // 重置表单
    resetForm() {
      this.$refs.formRef?.resetFields();
      this.form = {
        username: "",
        password: "",
        checkpassword: "",
        userRole: 0,
      };
    },
    
    // 🔴 修改：重置编辑表单
    resetEditForm() {
      if (this.$refs.editFormRef) {
        this.$refs.editFormRef.clearValidate();
      }
      this.editForm = {
        id: null,
        userName: "",
        changePassword: false,
        newPassword: "",
        confirmPassword: "",
        userRole: 0,
      };
    },
    
    // 🔴 新增：处理密码修改开关变化
    handlePasswordChangeSwitch(value) {
      if (!value) {
        // 如果关闭修改密码，清空密码字段并清除验证
        this.editForm.newPassword = "";
        this.editForm.confirmPassword = "";
        this.$nextTick(() => {
          if (this.$refs.editFormRef) {
            this.$refs.editFormRef.clearValidate(['newPassword', 'confirmPassword']);
          }
        });
      }
    },
    
    // 统一加载数据
    loadData() {
      if (this.searchMode && this.currentKeyword) {
        this.searchData();
      } else {
        this.loadPageData();
      }
    },
    
    // 加载分页数据
    loadPageData() {
      this.loading = true;
      axios
        .get(`/users?page=${this.currentPage}&pageSize=${this.pageSize}`)
        .then((response) => {
          this.tableData = response.data.data.rows || [];
          this.total = response.data.data.total || 0;
          this.loading = false;
        })
        .catch((error) => {
          console.error("Error fetching users:", error);
          this.$message.error("加载用户数据失败");
          this.loading = false;
        });
    },
    
    // 搜索数据
    searchData() {
      this.loading = true;
      const keyword = this.currentKeyword || this.formInline.keyword.trim();
      
      axios
        .get(`/findUser?keyword=${keyword}&page=${this.currentPage}&pageSize=${this.pageSize}`)
        .then((response) => {
          this.tableData = response.data.data.rows || [];
          this.total = response.data.data.total || 0;
          this.loading = false;
        })
        .catch((error) => {
          console.error("Error searching users:", error);
          this.$message.error("搜索用户失败");
          this.loading = false;
        });
    },
    
    // 分页变化处理
    handlePageChange(page) {
      this.currentPage = page;
      this.loadData();
    },
    
    // 搜索按钮点击
    onSearch() {
      const keyword = this.formInline.keyword.trim();
      if (!keyword) {
        this.$message.warning("请输入搜索关键词");
        return;
      }
      
      this.currentPage = 1;
      this.currentKeyword = keyword;
      this.searchMode = true;
      this.searchData();
    },
    
    // 添加新用户
    onAddNewUser() {
      this.$refs.formRef.validate((valid) => {
        if (!valid) {
          return;
        }
        
        this.adding = true;
        // 🔴 修改：使用form-data格式发送数据
        const formData = new FormData();
        formData.append('username', this.form.username);
        formData.append('password', this.form.password);
        formData.append('checkpassword', this.form.checkpassword);
        
        axios
          .post('/register', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          .then((response) => {
            if (response.data.code === 200) {
              this.$message({
                message: response.data.msg || "添加用户成功",
                type: "success",
                duration: 2000
              });
              
              this.dialogFormVisible = false;
              this.searchMode = false;
              this.currentKeyword = '';
              this.currentPage = 1;
              this.loadData();
            } else {
              this.$message.error(response.data.msg || "添加用户失败");
            }
            this.adding = false;
          })
          .catch((error) => {
            console.error("Error adding user:", error);
            this.$message.error("添加用户失败，请稍后重试");
            this.adding = false;
          });
      });
    },
    
    // 🔴 修改：编辑用户逻辑
    onEditUser() {
      this.$refs.editFormRef.validate((valid) => {
        if (!valid) {
          return;
        }
        
        // 构建提交数据
        const submitData = {
          id: this.editForm.id,
          userName: this.editForm.userName,
          userRole: this.editForm.userRole
        };
        
        // 只有在修改密码开关打开且新密码不为空时才传递密码
        if (this.editForm.changePassword && this.editForm.newPassword) {
          submitData.userPassword = this.editForm.newPassword;
        }
        
        this.editing = true;
        axios
          .post("/updateUser", submitData)
          .then((response) => {
            if (response.data.code === 200) {
              this.$message({
                message: response.data.msg || "用户信息更新成功",
                type: "success",
                duration: 2000
              });
              
              this.editDialogFormVisible = false;
              this.loadData();
            } else {
              this.$message.error(response.data.msg || "更新用户失败");
            }
            this.editing = false;
          })
          .catch((error) => {
            console.error("Error updating user:", error);
            this.$message.error("更新用户失败，请稍后重试");
            this.editing = false;
          });
      });
    },
    
    // 编辑按钮点击
    handleEdit(row) {
      // 🔴 修改：初始化编辑表单
      this.editForm = {
        id: row.id,
        userName: row.userName,
        changePassword: false, // 默认不修改密码
        newPassword: "",       // 新密码字段留空
        confirmPassword: "",   // 确认密码字段留空
        userRole: row.userRole || 0,
      };
      this.editDialogFormVisible = true;
    },
    
    // 删除用户
    handleDelete(row) {
      const id = row.id;
      this.$confirm("此操作将永久删除该用户, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          axios
            .get(`/deleteById?id=${id}`)
            .then((response) => {
              if (response.data.code === 200) {
                this.$message({
                  message: response.data.msg || "删除用户成功",
                  type: "success"
                });
                this.loadData();
              } else {
                this.$message.error(response.data.msg || "删除用户失败");
              }
            })
            .catch((error) => {
              console.error("Error deleting user:", error);
              this.$message.error("删除用户失败，请稍后重试");
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    logout() {
      // 删除 token
      localStorage.removeItem('jwt_token');
      // 跳转到登录页面
      this.$router.push('/login');
      this.$message({
        type: 'success',
        message: '已退出登录',
      });
    }
  },
  mounted() {
    this.loadData();
  }
};
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}

#app {
  height: 100%;
  width: 100%;
}

.el-header {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
  padding: 0 !important;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
}

.el-menu {
  text-align: left;
  height: 100%;
}

.el-main {
  padding: 20px;
  overflow: auto;
}

.el-footer {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.no-underline {
  text-decoration: none;
  color: inherit;
}

.no-underline:hover {
  text-decoration: none;
}

.el-table {
  flex: 1;
}

.el-pagination {
  margin-top: 20px;
  text-align: center;
}

.demo-form-inline {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.el-table .cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 🔴 新增：美化标签样式 */
.el-tag {
  margin-right: 5px;
}
</style>