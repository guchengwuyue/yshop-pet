import { isCustomFieldKey } from './formConfig'
import request from '@/config/axios'

const EXT_PREFIX = 'crm_clues_ext_'

function storageKey(clueId: number | string): string {
  return `${EXT_PREFIX}${clueId}`
}

/** 从表单数据中挑出自定义字段 */
export function pickCustomFields(formData: Record<string, any>): Record<string, any> {
  const result: Record<string, any> = {}
  for (const key of Object.keys(formData || {})) {
    if (isCustomFieldKey(key)) {
      result[key] = formData[key]
    }
  }
  return result
}

/** 读取线索自定义字段 */
export async function getCluesExt(clueId: number | string | undefined | null): Record<string, any> {
  if (clueId === undefined || clueId === null || clueId === '') return {}
  try {
    //const raw = localStorage.getItem(storageKey(clueId))
    const res = await request.get({ url: `/crm/form-data/get`, params: { module :'clues',id:clueId} })
    console.log('res', res.formDataJson)
    return JSON.parse(res.formDataJson) 
    
    //if (!raw) return {}
    //console.log('raw', JSON.parse(raw))
    //return JSON.parse(raw) as Record<string, any>
  } catch {
    return {}
  }
}

/** 保存线索自定义字段 */
export async function saveCluesExt(
  clueId: number | string | undefined | null,
  data: Record<string, any>
): void {
  if (clueId === undefined || clueId === null || clueId === '') return
  const custom = pickCustomFields(data)
  console.log('custom', custom)
  await request.post({ url: `/crm/form-data/create`, data: { formModule:'clues', formDataId:clueId,
    formDataJson: JSON.stringify(custom) } })
  l//ocalStorage.setItem(storageKey(clueId), JSON.stringify(custom))
}

/** 删除线索自定义字段缓存 */
export function removeCluesExt(clueId: number | string | undefined | null): void {
  if (clueId === undefined || clueId === null || clueId === '') return
  localStorage.removeItem(storageKey(clueId))
}

/** 将自定义字段合并到行数据 */
export function mergeCluesExt<T extends { id?: number | string }>(row: T): T & Record<string, any> {
  if (row?.id == null) return { ...row }
  return { ...row, ...getCluesExt(row.id) }
}

/** 批量 merge */
export function mergeCluesExtList<T extends { id?: number | string }>(
  rows: T[]
): Array<T & Record<string, any>> {
  return (rows || []).map((row) => mergeCluesExt(row))
}
