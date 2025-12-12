<template>
  <!-- 修改：添加 height: 100vh 让容器占满整个视口 -->
  <el-container style="height: 100vh;">
  <!-- <el-container> -->
    <el-header style="font-size: 40px; background-color: rgb(238, 241, 246)">
      Quiz后台管理

      <!-- 右侧退出按钮 -->
      <div style="position:absolute; right:20px; top:-1px;">
        <el-button type="danger" size="mini" @click="logout">退出</el-button>
      </div>
    </el-header>
    <!-- <el-header>Header</el-header> -->

    <!-- 修改：移除固定高度，使用 flex: 1 填充剩余空间 -->
    <el-container style="flex: 1; border: 1px solid #eee">
    <!-- <el-container style="height: 500px; border: 1px solid #eee"> -->
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <el-menu :default-openeds="['1']">
          <!-- <el-menu :default-openeds="['1', '3']">-->
          <el-submenu index="1">
            <template slot="title">管理选项</template>
            <el-menu-item-group>
              <!-- <template slot="title">分组一</template> -->
              <el-menu-item index="1-1">
                <!-- 用户管理 -->
                <!-- <router-link to="/user">用户管理</router-link> -->
                 <router-link to="/user" class="no-underline">用户管理</router-link>
              </el-menu-item>
              <el-menu-item index="1-2">
                <!-- 题目管理 -->
                <!-- <router-link to="/question">题目管理</router-link> -->
                <router-link to="/question" class="no-underline">题目管理</router-link>
              </el-menu-item>
            </el-menu-item-group>

            <!-- <el-menu-item-group title="分组2">
              <el-menu-item index="1-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="1-4">
              <template slot="title">选项4</template>
              <el-menu-item index="1-4-1">选项4-1</el-menu-item>
            </el-submenu> -->
          </el-submenu>

          <!-- <el-submenu index="2">
            <template slot="title"><i class="el-icon-menu"></i>导航二</template>
            <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item index="2-1">选项1</el-menu-item>
              <el-menu-item index="2-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="2-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item index="2-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title"
              ><i class="el-icon-setting"></i>导航三</template
            >
            <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item index="3-1">选项1</el-menu-item>
              <el-menu-item index="3-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="3-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="3-4">
              <template slot="title">选项4</template>
              <el-menu-item index="3-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu> -->
        </el-menu>
      </el-aside>

      <el-main>
        <!-- 顶部查询的表单 -->
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item label="题目">
            <!-- 修改：添加了 clearable 属性和回车事件监听 -->
            <el-input
              v-model="formInline.keyword"
              placeholder="请输入题目关键词"
              @keyup.enter.native="onSearch"
              clearable
            ></el-input>
          </el-form-item>
          <!-- <el-form-item label="活动区域">
            <el-select v-model="formInline.region" placeholder="活动区域">
              <el-option label="区域一" value="shanghai"></el-option>
              <el-option label="区域二" value="beijing"></el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item>
            <el-button type="primary" @click="onSearch">查询</el-button>
          </el-form-item>
          <!-- 在这里添加重置按钮 -->
          <el-form-item>
            <el-button @click="onReset">重置</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="addNewQuestion"
              >添加题目</el-button
            >
          </el-form-item>
        </el-form>

        <!-- 显示的table -->
         <!-- 修改：添加了加载状态指示器 -->
        <el-table :data="tableData" style="width: 100%" v-loading="loading">
          <el-table-column label="序号" width="80">
            <template slot-scope="scope">
              <!-- <i class="el-icon-time"></i> -->
              <span style="margin-left: 10px">{{ scope.row.id }}</span>
            </template>
          </el-table-column>
          <el-table-column label="题目" width="240">
            <template slot-scope="scope">
              <!-- <el-popover trigger="hover" placement="top"> -->
              <!-- <p>姓名: {{ scope.row.name }}</p>
                <p>住址: {{ scope.row.address }}</p> -->
              <div slot="reference" class="name-wrapper">
                <!-- <el-tag size="medium">{{ scope.row.question }}</el-tag> -->
                <span style="margin-left: 10px">{{ scope.row.questionText }}</span>
              </div>
              <!-- </el-popover> -->
            </template>
          </el-table-column>
          <el-table-column label="选项" width="300">
            <template slot-scope="scope">
              <!-- 修改：优化了选项显示格式，使用A、B、C、D标号 -->
              <!-- <i class="el-icon-time"></i> -->
              <!-- <span style="margin-left: 10px">{{ scope.row.options }}</span> -->
              <div>
                <div v-for="(option, index) in scope.row.options" :key="index">
                  {{ ['A', 'B', 'C', 'D'][index] }}. {{ option }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="答案" width="180">
            <template slot-scope="scope">
              <!-- <i class="el-icon-time"></i> -->
              <span style="margin-left: 10px">{{ scope.row.answer }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)"
                >编辑</el-button
              >
              <!-- <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)"
                >删除</el-button
              > -->
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <br />
        <!-- 分页 -->
         <!-- 新增：显示总条数,单页时隐藏分页 -->
        <el-pagination background layout="prev, pager, next, total"
          :page-size="pageSize"
          :total="total"
          :current-page="currentPage"
          @current-change="handlePageChange"
          :hide-on-single-page="total <= pageSize"
        >
        </el-pagination>

        <!-- 弹出的对话表格对话框 -->
         <!-- 新增：点击遮罩不关闭, 关闭时重置表单 -->
        <el-dialog title="添加题目" 
        :visible.sync="dialogFormVisible"
        :close-on-click-modal="false"
          @closed="resetForm"
        >
          <!-- <el-form :model="form">
            <el-form-item label="题目" :label-width="formLabelWidth">
              <el-input v-model="form.question" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="选项A" :label-width="formLabelWidth">
              <el-input v-model="form.optiona" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="选项B" :label-width="formLabelWidth">
              <el-input v-model="form.optionb" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="选项C" :label-width="formLabelWidth">
              <el-input v-model="form.optionc" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="选项D" :label-width="formLabelWidth">
              <el-input v-model="form.optiond" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="答案" :label-width="formLabelWidth">
              <el-input v-model="form.answer" autocomplete="off"></el-input>
            </el-form-item>
          </el-form> -->
          <!-- 修改：表单添加了验证规则和引用 -->
          <el-form :model="form" :rules="formRules" ref="formRef">
            <el-form-item label="题目" :label-width="formLabelWidth" prop="question">
              <!-- 修改：添加了placeholder -->
              <el-input v-model="form.question" autocomplete="off" placeholder="请输入题目内容"></el-input>
            </el-form-item>
            <el-form-item label="选项A" :label-width="formLabelWidth" prop="optiona">
              <!-- 修改：添加了placeholder -->
              <el-input v-model="form.optiona" autocomplete="off" placeholder="请输入选项A内容"></el-input>
            </el-form-item>
            <el-form-item label="选项B" :label-width="formLabelWidth" prop="optionb">
              <!-- 修改：添加了placeholder -->
              <el-input v-model="form.optionb" autocomplete="off" placeholder="请输入选项B内容"></el-input>
            </el-form-item>
            <el-form-item label="选项C" :label-width="formLabelWidth" prop="optionc">
              <!-- 修改：添加了placeholder -->
              <el-input v-model="form.optionc" autocomplete="off" placeholder="请输入选项C内容"></el-input>
            </el-form-item>
            <el-form-item label="选项D" :label-width="formLabelWidth" prop="optiond">
              <!-- 修改：添加了placeholder -->
              <el-input v-model="form.optiond" autocomplete="off" placeholder="请输入选项D内容"></el-input>
            </el-form-item>
            <el-form-item label="答案" :label-width="formLabelWidth" prop="answer">
              <!-- 修改：将输入框改为下拉选择框 -->
              <!-- <el-input v-model="form.answer" autocomplete="off"></el-input> -->
              <el-select v-model="form.answer" placeholder="请选择正确答案" style="width: 100%">
                <el-option label="A" value="a"></el-option>
                <el-option label="B" value="b"></el-option>
                <el-option label="C" value="c"></el-option>
                <el-option label="D" value="d"></el-option>
              </el-select>
            </el-form-item>
          </el-form>

          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <!-- <el-button type="primary" @click="dialogFormVisible = false"
              >确 定</el-button
            > -->
            <!-- 修改：确定按钮添加了加载状态 -->
            <el-button type="primary" @click="onAddNewQuestion" :loading="adding"
              >确 定</el-button
            >
          </div>
        </el-dialog>

        <!-- 在添加对话框之后添加编辑对话框 -->
        <el-dialog title="编辑题目" 
          :visible.sync="editDialogFormVisible"
          :close-on-click-modal="false"
          @closed="resetEditForm">
          
          <el-form :model="editForm" :rules="editFormRules" ref="editFormRef">
            <el-form-item label="题目" :label-width="formLabelWidth" prop="question">
              <el-input v-model="editForm.question" autocomplete="off" placeholder="请输入题目内容"></el-input>
            </el-form-item>
            <el-form-item label="选项A" :label-width="formLabelWidth" prop="optiona">
              <el-input v-model="editForm.optiona" autocomplete="off" placeholder="请输入选项A内容"></el-input>
            </el-form-item>
            <el-form-item label="选项B" :label-width="formLabelWidth" prop="optionb">
              <el-input v-model="editForm.optionb" autocomplete="off" placeholder="请输入选项B内容"></el-input>
            </el-form-item>
            <el-form-item label="选项C" :label-width="formLabelWidth" prop="optionc">
              <el-input v-model="editForm.optionc" autocomplete="off" placeholder="请输入选项C内容"></el-input>
            </el-form-item>
            <el-form-item label="选项D" :label-width="formLabelWidth" prop="optiond">
              <el-input v-model="editForm.optiond" autocomplete="off" placeholder="请输入选项D内容"></el-input>
            </el-form-item>
            <el-form-item label="答案" :label-width="formLabelWidth" prop="answer">
              <el-select v-model="editForm.answer" placeholder="请选择正确答案" style="width: 100%">
                <el-option label="A" value="a"></el-option>
                <el-option label="B" value="b"></el-option>
                <el-option label="C" value="c"></el-option>
                <el-option label="D" value="d"></el-option>
              </el-select>
            </el-form-item>
          </el-form>

          <div slot="footer" class="dialog-footer">
            <el-button @click="editDialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="onEditQuestion" :loading="editing">确 定</el-button>
          </div>
        </el-dialog>
      </el-main>
    </el-container>

    <!-- <el-container>
      <el-aside width="200px">Aside</el-aside>
      <el-main>Main</el-main>
    </el-container> -->
  </el-container>
</template>


<script>
import axios from 'axios';

export default {
  data() {
    return {
      dialogFormVisible: false,
      editDialogFormVisible: false, // 新增：编辑对话框显示状态
      loading: false,           // 新增：加载状态
      adding: false,            // 新增：添加中状态
      editing: false,           // 新增：编辑中状态
      currentPage: 1,
      pageSize: 5, // 每页显示的条数
      total: 0,    // 总条数
      searchMode: false, // 标记是否在搜索模式
      formInline: {
        keyword: "",
        // region: "",
      },
      tableData: [
        // {
        //   id: 1,
        //   question: "法国的首都是哪个城市？",
        //   options: "巴黎, 华盛顿, 东京, 北京",
        //   answer: "巴黎",
        // },
        // {
        //   id: 2,
        //   question: "法国的首都是哪个城市？",
        //   options: "巴黎, 华盛顿, 东京, 北京",
        //   answer: "巴黎",
        // },
        // {
        //   id: 3,
        //   question: "法国的首都是哪个城市？",
        //   options: "巴黎, 华盛顿, 东京, 北京",
        //   answer: "巴黎",
        // },
        // {
        //   id: 4,
        //   question: "法国的首都是哪个城市？",
        //   options: "巴黎, 华盛顿, 东京, 北京",
        //   answer: "巴黎",
        // },
      ],
      form: {
        question: "",
        optiona: "",
        optionb: "",
        optionc: "",
        optiond: "",
        answer: "",
      },
      editForm: { // 新增：编辑表单数据
        id: null,
        question: "",
        optiona: "",
        optionb: "",
        optionc: "",
        optiond: "",
        answer: "",
      },
      formLabelWidth: "120px",
      formRules: {             // 新增：表单验证规则
        question: [
          { required: true, message: '请输入题目内容', trigger: 'blur' }
        ],
        optiona: [
          { required: true, message: '请输入选项A内容', trigger: 'blur' }
        ],
        optionb: [
          { required: true, message: '请输入选项B内容', trigger: 'blur' }
        ],
        optionc: [
          { required: true, message: '请输入选项C内容', trigger: 'blur' }
        ],
        optiond: [
          { required: true, message: '请输入选项D内容', trigger: 'blur' }
        ],
        answer: [
          { required: true, message: '请选择正确答案', trigger: 'change' }
        ]
      },
    };
  },
  methods: {
    addNewQuestion() {
      this.dialogFormVisible = true;
    },
    onSubmit() {
      console.log("submit!");
    },
    // handleEdit(index, row) {
    //   console.log(index, row);
    //   // 新增：编辑功能提示
    //   this.$message.info("编辑功能暂未实现");
    // },
    // handleDelete(index, row) {
    //   console.log(index, row);
    // },

    // handlePageChange(page) {
    //   this.currentPage = page;
    //   axios
    //     .get(`/questions?page=${this.currentPage}&pageSize=${this.pageSize}`)
    //     .then((response) => {
    //       this.tableData = response.data.data.qsBeanList;
    //       this.total = response.data.data.total;
    //     })
    //     .catch((error) => {
    //       console.error("Error fetching questions:", error);
    //     });
    // },

    // onSearch() {
    //   // 这里可以添加查询逻辑
    //   console.log("Searching for:", this.formInline.keyword);
    //   // 例如，调用API获取数据
    //   axios
    //     .get(`/findQuestion?keyword=${this.formInline.keyword}`)
    //     .then((response) => {
    //       this.tableData = response.data.data;
    //       console.log("Search results:", response.data);
    //       // this.total = response.data.data.total;
    //     })
    //     .catch((error) => {
    //       console.error("Error searching questions:", error);
    //     });
    // },

    // onAddNewQuestion() {
    //   console.log("Submitting question:", this.form,{
    //       headers: {
    //         "Content-Type": "application/json",
    //       },
    //     });
    //   axios
    //     .post("/addQuestion", this.form)
    //     .then((response) => {
    //       console.log("Question added successfully:", response.data);
    //       this.dialogFormVisible = false;
    //       this.handlePageChange(this.currentPage);
    //     })
    //     .catch((error) => {
    //       console.error("Error adding question:", error);
    //     });
    // },

    // handleDelete(row) {
    //   const id = row.id; // 获取当前条目的 id
    //   this.$confirm("此操作将永久删除该题目, 是否继续?", "提示", {
    //     confirmButtonText: "确定",
    //     cancelButtonText: "取消",
    //     type: "warning",
    //   })
    //     .then(() => {
    //       // 调用删除接口（GET 请求）
    //       axios
    //         .get(`/delQuestion?id=${id}`) // 使用 GET 请求传递 id 参数
    //         .then((response) => {
    //           console.log(response.data);
    //           // 删除成功后刷新当前页数据
    //           this.handlePageChange(this.currentPage);
    //         })
    //         .catch((error) => {
    //           console.error("Error deleting question:", error);
    //           this.$message({
    //             type: "error",
    //             message: "删除失败，请稍后重试!",
    //           });
    //         });
    //     })
    //     .catch(() => {
    //       this.$message({
    //         type: "info",
    //         message: "已取消删除",
    //       });
    //     });
    // },

    // 修改：分页变化处理
    handlePageChange(page) {
      this.currentPage = page;
      // 调用统一的加载数据方法
      this.loadData();
    },

    // 修改：搜索方法，支持分页搜索
    onSearch() {
      // 这里可以添加查询逻辑
      console.log("Searching for:", this.formInline.keyword);
      // 重置到第一页并设置搜索模式
      this.currentPage = 1;
      this.searchMode = true;
      // 调用搜索方法
      this.searchData();
    },

    // 在 methods 中添加重置方法
    onReset() {
      // 清空搜索关键词
      this.formInline.keyword = "";
      // 退出搜索模式
      this.searchMode = false;
      // 重置到第一页
      this.currentPage = 1;
      // 重新加载数据（普通分页数据）
      this.loadData();
    },

    // 修改：添加新题目方法，添加表单验证和成功提示
    onAddNewQuestion() {
      // 新增：表单验证
      this.$refs.formRef.validate((valid) => {
        if (!valid) {
          return;
        }
        
        console.log("Submitting question:", this.form);
        this.adding = true;  // 显示加载状态
        axios
          .post("/addQuestion", this.form)
          .then((response) => {
            console.log("Question added successfully:", response.data);
            
            // 修改：将成功码判断从 200 改为 1
            if (response.data.code === 1) {
              this.$message({
                message: response.data.msg || "添加问题成功",
                type: "success",
                duration: 2000
              });
              this.dialogFormVisible = false; // 成功后关闭对话框
              // 重置搜索状态，回到第一页
              this.searchMode = false;
              this.currentPage = 1;
              this.loadData(); // 重新加载数据
            } else {
              this.$message.error(response.data.msg || "添加失败");
            }
            this.adding = false;  // 隐藏加载状态
          })
          .catch((error) => {
            console.error("Error adding question:", error);
            this.$message.error("添加失败，请稍后重试");
            this.adding = false;  // 隐藏加载状态
          });
      });
    },

    // 修改：删除方法，添加成功提示
    handleDelete(row) {
      const id = row.id; // 获取当前条目的 id
      this.$confirm("此操作将永久删除该题目, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // 调用删除接口（GET 请求）
          axios
            .get(`/delQuestion?id=${id}`) // 使用 GET 请求传递 id 参数
            .then((response) => {
              console.log(response.data);
              // 修改：将成功码判断从 200 改为 1
              if (response.data.code === 1) {
                this.$message({
                  message: response.data.msg || "删除成功",
                  type: "success"
                });
                // 删除成功后刷新当前页数据
                this.loadData();
              } else {
                this.$message.error(response.data.msg || "删除失败");
              }
            })
            .catch((error) => {
              console.error("Error deleting question:", error);
              this.$message({
                type: "error",
                message: "删除失败，请稍后重试!",
              });
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    
    // 新增：重置表单方法
    resetForm() {
      if (this.$refs.formRef) {
        this.$refs.formRef.resetFields();
      }
      this.form = {
        question: "",
        optiona: "",
        optionb: "",
        optionc: "",
        optiond: "",
        answer: "",
      };
    },
    
    // 新增：统一加载数据方法
    loadData() {
      if (this.searchMode && this.formInline.keyword.trim()) {
        this.searchData();
      } else {
        this.loadPageData();
      }
    },
    
    // 新增：加载分页数据方法
    loadPageData() {
      this.loading = true;
      axios
        .get(`/questions?page=${this.currentPage}&pageSize=${this.pageSize}`)
        .then((response) => {
          this.tableData = response.data.data.qsBeanList;
          this.total = response.data.data.total;
          this.loading = false;
        })
        .catch((error) => {
          console.error("Error fetching questions:", error);
          this.$message.error("加载数据失败");
          this.loading = false;
        });
    },
    
    // 新增：搜索数据方法
    searchData() {
      this.loading = true;
      const keyword = this.formInline.keyword.trim();
      axios
        .get(`/findQuestion?keyword=${keyword}&page=${this.currentPage}&pageSize=${this.pageSize}`)
        .then((response) => {
          this.tableData = response.data.data.qsBeanList || [];
          this.total = response.data.data.total || 0;
          this.loading = false;
          
          //// 新增：清空搜索框
          // this.formInline.keyword = "";
        })
        .catch((error) => {
          console.error("Error searching questions:", error);
          this.$message.error("搜索失败");
          this.loading = false;
        });
    },

        handleEdit(index, row) {
      console.log("编辑题目:", index, row);
      // 将当前行的数据填充到编辑表单中
      this.editForm.id = row.id;
      this.editForm.question = row.questionText || "";
      
      // 将options数组拆分为四个选项
      if (row.options && row.options.length >= 4) {
        this.editForm.optiona = row.options[0] || "";
        this.editForm.optionb = row.options[1] || "";
        this.editForm.optionc = row.options[2] || "";
        this.editForm.optiond = row.options[3] || "";
      } else {
        this.editForm.optiona = "";
        this.editForm.optionb = "";
        this.editForm.optionc = "";
        this.editForm.optiond = "";
      }
      
      // 根据答案文本确定答案选项
      const answerText = row.answer || "";
      const options = row.options || [];
      let answerValue = "";
      
      // 查找答案对应的选项
      if (options.length >= 4) {
        if (answerText === options[0]) {
          answerValue = "a";
        } else if (answerText === options[1]) {
          answerValue = "b";
        } else if (answerText === options[2]) {
          answerValue = "c";
        } else if (answerText === options[3]) {
          answerValue = "d";
        }
      }
      
      this.editForm.answer = answerValue;
      
      // 显示编辑对话框
      this.editDialogFormVisible = true;
    },

        // 新增：编辑题目提交
    onEditQuestion() {
      this.$refs.editFormRef.validate((valid) => {
        if (!valid) {
          return;
        }
        
        console.log("提交编辑题目:", this.editForm);
        this.editing = true; // 显示加载状态
        
        axios
          .post("/updateQuestion", this.editForm)
          .then((response) => {
            console.log("编辑题目成功:", response.data);
            this.editing = false;
            
            // 修改：将成功码判断从 200 改为 1
            if (response.data.code === 1) {
              this.$message({
                message: response.data.msg || "编辑问题成功",
                type: "success",
                duration: 2000
              });
              this.editDialogFormVisible = false;
              // 刷新当前页数据
              this.loadData();
            } else {
              this.$message.error(response.data.msg || "编辑失败");
            }
          })
          .catch((error) => {
            console.error("编辑题目失败:", error);
            this.$message.error("编辑失败，请稍后重试");
            this.editing = false;
          });
      });
    },

        // 新增：重置编辑表单
    resetEditForm() {
      if (this.$refs.editFormRef) {
        this.$refs.editFormRef.resetFields();
      }
      this.editForm = {
        id: null,
        question: "",
        optiona: "",
        optionb: "",
        optionc: "",
        optiond: "",
        answer: "",
      };
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
    },
  },
  mounted(){
    // 修改：初始化数据加载
    this.loadData();
    // // 初始化第一页数据
    // this.handlePageChange(1);
    // axios.get("/questions").then((response) => {
    //   // 假设返回的数据是一个数组
    //   console.log(response.data);
    //   this.tableData = response.data.data.qsBeanList; // 更新表格数据
    //   this.total = response.data.data.total; // 动态设置总条数
    // }).catch((error) => {
    //   console.error("Error fetching questions:", error);
    // });
	}
};
</script>


<style>

/* 新增：确保html和body占满整个视口 */
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}

/* 新增：全局样式，确保Element UI容器正常工作 */
#app {
  height: 100%;
  width: 100%;
}

/* 修改：最外层容器设置，移除原来的固定高度 */
.el-header {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
  padding: 0 !important; /* 移除内边距 */
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
}

/* 修改：移除el-aside的line-height，避免侧边栏内容垂直居中问题 */
.el-menu {
  text-align: left;
  height: 100%;
}

.el-main {
  padding: 20px;
  overflow: auto; /* 内容过多时显示滚动条 */
}


/* .el-header, */
.el-footer {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

/* .el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
} */

.no-underline {
  text-decoration: none; /* 去除下划线 */
  color: inherit; /* 继承链接颜色 */
}

.no-underline:hover {
  text-decoration: none; /* 去除悬停时的下划线 */
}

/* 优化选项显示样式 */
.el-table .cell {
  white-space: pre-line;
}

/* 新增：确保表格容器自适应 */
.el-table {
  flex: 1;
}

/* 新增：让分页组件在底部 */
.el-pagination {
  margin-top: 20px;
  /* text-align: center; */
}

/* 修改：确保表单在一行内显示 */
.demo-form-inline {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}
</style>