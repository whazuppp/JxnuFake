<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { queryStudentsApi } from '@/api/stu'

const keyword = ref('')
const loading = ref(false)
const rows = ref([])
const searched = ref(false)
const queryMode = ref('condition')
const field = ref('name')
const exact = ref(true)

const fieldOptions = [
  { label: '姓名', value: 'name' },
  { label: '学号', value: 'stuId' }
]

const matchOptions = [
  { label: '精确', value: true },
  { label: '模糊', value: false }
]

const submitQuery = async () => {
  if (!keyword.value.trim()) {
    ElMessage.warning('请输入关键字！')
    return
  }

  loading.value = true
  searched.value = true

  try {
    const result = await queryStudentsApi({
      field: field.value,
      keyword: keyword.value.trim(),
      exact: exact.value
    })
    rows.value = result.code === 1 ? (result.data || []) : []
    if (result.code !== 1) {
      ElMessage.error(result.msg || '查询失败')
    }
  } catch {
    ElMessage.error('查询失败')
    rows.value = []
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="directory-page">
    <div class="query-card">
      <div class="query-mode">
        <el-radio v-model="queryMode" label="condition">条件查询</el-radio>
      </div>

      <div class="query-row">
        <el-input
          v-model="keyword"
          class="keyword-input"
          placeholder="请输入关键字!"
          @keyup.enter="submitQuery"
        />
        <el-select v-model="field" class="field-select">
          <el-option
            v-for="option in fieldOptions"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </el-select>
        <el-select v-model="exact" class="match-select">
          <el-option
            v-for="option in matchOptions"
            :key="option.label"
            :label="option.label"
            :value="option.value"
          />
        </el-select>
        <el-button type="primary" @click="submitQuery">查询</el-button>
      </div>
    </div>

    <div class="result-card">
      <el-table v-loading="loading" :data="rows" empty-text="暂无数据">
        <el-table-column prop="name" label="姓名" min-width="120" />
        <el-table-column prop="studentNo" label="学号" min-width="160" />
        <el-table-column prop="gender" label="性别" min-width="90">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="className" label="班级" min-width="240" />
      </el-table>

      <el-empty
        v-if="searched && !loading && rows.length === 0"
        class="empty-state"
        description="未查询到符合条件的学生信息"
      />
    </div>
  </div>
</template>

<style scoped>
.directory-page {
  min-height: 100%;
  background: #fff;
}

.query-card {
  padding: 28px 24px 18px;
}

.query-mode {
  display: flex;
  justify-content: center;
  margin-bottom: 14px;
  color: #1f5fbf;
}

.query-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  flex-wrap: wrap;
}

.keyword-input {
  width: 360px;
}

.field-select,
.match-select {
  width: 96px;
}

.result-card {
  padding: 0 24px 24px;
}

.empty-state :deep(.el-empty__image) {
  display: none;
}

@media (max-width: 768px) {
  .query-card {
    padding: 20px 16px 12px;
  }

  .query-row {
    align-items: stretch;
  }

  .keyword-input,
  .field-select,
  .match-select {
    width: 100%;
  }

  .result-card {
    padding: 0 16px 20px;
  }
}
</style>
