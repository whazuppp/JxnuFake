import { readFileSync } from 'node:fs'
import { fileURLToPath } from 'node:url'
import { describe, expect, it } from 'vitest'

const baseinfoSource = readFileSync(
  fileURLToPath(new URL('./baseinfo.vue', import.meta.url)),
  'utf8'
)

describe('baseinfo table structure', () => {
  it('wraps each info table row inside tbody', () => {
    const tableBlocks = [...baseinfoSource.matchAll(/<table class="info-table">([\s\S]*?)<\/table>/g)]

    expect(tableBlocks).toHaveLength(2)

    tableBlocks.forEach(([, tableBlock]) => {
      const normalizedBlock = tableBlock.trim()

      expect(normalizedBlock.startsWith('<tbody>')).toBe(true)
      expect(normalizedBlock.endsWith('</tbody>')).toBe(true)
      expect(normalizedBlock).not.toMatch(/<\/tbody>[\s\S]*<tr>/)
    })
  })
})
