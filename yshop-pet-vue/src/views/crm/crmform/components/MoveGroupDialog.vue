<template>
  <Dialog title="移动分组" v-model="visible" width="420px">
    <el-form label-width="90px">
      <el-form-item label="目标分组">
        <el-select v-model="targetGroupId" placeholder="请选择目标分组" class="w-full">
          <el-option
            v-for="g in availableGroups"
            :key="g.id"
            :label="g.name"
            :value="g.id"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取 消</el-button>
      <el-button type="primary" @click="handleConfirm">确 定</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import type { FormGroup } from '../types'

defineOptions({ name: 'MoveGroupDialog' })

const props = defineProps<{
  groups: FormGroup[]
  currentGroupId: string
}>()

const emit = defineEmits<{
  confirm: [targetGroupId: string]
}>()

const visible = ref(false)
const targetGroupId = ref('')

const availableGroups = computed(() =>
  props.groups.filter((g) => g.id !== props.currentGroupId)
)

const open = () => {
  targetGroupId.value = availableGroups.value[0]?.id || ''
  visible.value = true
}

const handleConfirm = () => {
  if (!targetGroupId.value) {
    return
  }
  emit('confirm', targetGroupId.value)
  visible.value = false
}

defineExpose({ open })
</script>
