<script setup>
import { ref, onMounted } from 'vue'
import { queryAllCoursesApi, addCourseApi, queryCourseByIdApi, updateCourseApi, deleteCourseApi } from '@/api/course'
import { ElMessage, ElMessageBox } from 'element-plus'

// 课程列表
const courseList = ref([])

// 查询所有课程
const queryAll = async () => {
  const result = await queryAllCoursesApi()
  if (result.code) {
    courseList.value = result.data
  }
}

// 页面加载时获取数据
onMounted(() => {
  queryAll()
})

// 表单标题
const formTitle = ref('')

// 控制对话框显示
const showDialog = ref(false)

// 表单数据
const courseForm = ref({
  name: '',
  number: null,
  teacherName: ''
})

// 表单验证规则
const formRules = ref({
  name: [
    { required: true, message: '请输入课程名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  number: [
    { required: true, message: '请输入课时数', trigger: 'blur' },
    { type: 'number', message: '课时数必须为数字', trigger: 'blur' }
  ],
  teacherName: [
    { required: true, message: '请输入授课老师', trigger: 'blur' }
  ]
})

// 表单引用
const courseFormRef = ref(null)

// 重置表单
const resetForm = () => {
  courseFormRef.value?.resetFields()
}

// 新增课程
const add = () => {
  formTitle.value = '新增课程'
  showDialog.value = true
  courseForm.value = { name: '', number: null, teacherName: '' }
}

// 提交表单
const save = async () => {
  await courseFormRef.value.validate(async valid => {
    if (!valid) return
    let result = null
    if (courseForm.value.id) {
      result = await updateCourseApi(courseForm.value)
    } else {
      result = await addCourseApi(courseForm.value)
    }

    if (result.code) {
      ElMessage.success('操作成功')
      showDialog.value = false
      resetForm()
      queryAll()
    } else {
      ElMessage.error(result.msg)
    }
  })
}

// 编辑课程
const handleEdit = async (id) => {
  formTitle.value = '修改课程'
  showDialog.value = true
  courseForm.value = { name: '', number: null, teacherName: '' }

  const result = await queryCourseByIdApi(id)
  if (result.code) {
    courseForm.value = result.data
  }
}

// 删除课程
const handleDelete = async (id) => {
  ElMessageBox.confirm('此操作将永久删除该课程, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await deleteCourseApi(id)
    if (result.code) {
      ElMessage.success('删除成功')
      queryAll()
    } else {
      ElMessage.error(result.msg)
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}
</script>

<template>
  <h1>课程管理</h1>

  <el-button type="primary" @click="add">+ 新增课程</el-button><br><br>

  <el-table :data="courseList" border style="width: 100%;">
    <el-table-column type="index" label="序号" width="100" align="center" />
    <el-table-column prop="name" label="课程名称" width="200" align="center" />
    <el-table-column prop="number" label="课时数" width="120" align="center" />
    <el-table-column prop="teacherName" label="授课老师" width="200" align="center" />
    <el-table-column prop="updateTime" label="最后修改时间" width="200" align="center" />
    <el-table-column fixed="right" label="操作" align="center">
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.row.id)">修改</el-button>
        <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- 课程对话框 -->
  <el-dialog v-model="showDialog" :title="formTitle" width="40%" @close="resetForm">
    <el-form :model="courseForm" :rules="formRules" ref="courseFormRef">
      <el-form-item label="课程名称" prop="name" label-width="100px">
        <el-input v-model="courseForm.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="课时数" prop="number" label-width="100px">
        <el-input v-model.number="courseForm.number" autocomplete="off" />
      </el-form-item>
      <el-form-item label="授课老师" prop="teacherName" label-width="100px">
        <el-input v-model="courseForm.teacherName" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
</style>
