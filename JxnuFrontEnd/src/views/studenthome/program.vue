<script setup>
import { ref } from 'vue'

const yearOptions = ['2023年', '2024年', '2025年', '2026年']
const majorOptions = ['计算机科学与技术（综合型）']

const selectedYear = ref('2023年')
const selectedMajor = ref('计算机科学与技术（综合型）')
const displayYear = ref('2023年')
const displayMajor = ref('计算机科学与技术（综合型）')

const submitQuery = () => {
  displayYear.value = selectedYear.value
  displayMajor.value = selectedMajor.value
}

const objectiveParagraphs = [
  '面向多学科交叉复合的领域应用需求，面向区域经济发展需要，培养具备计算思维和工程理念的，德、智、体、美全面发展，具有良好的思想品质与职业道德、工程意识和素质，具备较强的社会适应能力、系统设计能力、工程实践能力、自主学习能力、应，具备良好的书面和口头表达能力，富于批判精神、创新精神以及团队合作精神，具备国际视野和跨文化环境下的交流、合作与竞争能力，能够从事计算机软硬件系统的设计、研发、维护和应用等方面工作的专业人才。'
]

const requirementBlocks = [
  {
    title: '一般知识、能力、素养：',
    paragraphs: [
      '1) 掌握马克思主义基本原理、毛泽东思想和中国特色社会主义理论体系的基本内容。2) 了解古今中外人文、社会和科技领域的基本知识和成果，掌握自然环境与生态文明、法律与政治制度、国际关系与世界发展及祖国国情的基本知识。了解不同语言文化背景常识，掌握英语语音、语法、词汇、句法以及篇章的系统知识。了解网络技术、信息安全和程序设计，掌握计算机基础知识和常用办公软件的应用。3) 了解和掌握人的身心健康的基本知识，熟识一般体育锻炼、身心康复及安全运动常识。4) 了解美学美育基础知识，熟识马克思主义美学基本观点及其当代发展。',
      '1) 具备运用马克思主义立场、观点、方法分析和解决问题的基本能力。2) 具备基本的收集与判断资讯、适应社会生活和解决现实问题的能力。具有英语听、说、读、写、译的基本能力，有一定的跨文化交际能力。具有结合所学专业进行文字处理、数据处理、信息获取及程序设计的一般能力。3) 具备运动技能自学能力、体育项目的自我锻炼能力、锻炼效果的自我评价能力、运动中自我调节能力以及提高自我保健意识和心理健康的调节能力，掌握运动损伤的一般处理方法。4) 具备感受美、欣赏美和初步理解美、创造美的能力。',
      '1) 树立科学的世界观、人生观、价值观，具备良好的理想情操和道德品质，注重德性养成。2) 志存高远、视野宽广、勤于学习、勇于实践、善于反思，具备健全人格、珍爱生命、责任担当和国家认同。具有自觉提升跨文化交流借鉴的意识，熟识外事礼仪，与外宾沟通交流得体自然。有自觉运用信息化处理工作的意识和一定的信息文化素养。3) 养成以终身体育锻炼习惯为主的运动生活方式，具有主动学习、吃苦耐劳、坚忍不拔、团结协作等意志品质和健全体魄。4) 具有高尚的艺术鉴赏和审美情趣。'
    ]
  },
  {
    title: '专业知识、能力、素养：',
    paragraphs: [
      '（1）较为全面系统地掌握计算机科学的基础理论和基本方法，包括：1) 掌握支撑计算机科学知识体系的数学基础理论、方法和分析技术，以及针对计算机应用的数学分析、建模的基本技术和方法；2) 掌握基础编程技术、基本编程语言C、Java、JSP；3) 掌握计算机硬件组成的系统结构、组成原理和运作机制，以及对数字信号和逻辑处理单元的分析、设计和应用的基本方法和语言；4) 掌握操作系统的目标、模型和运作机制，以及相关实现技术和方法；5) 掌握计算机网络的基本原理、网络系统的规划与组建技术，以及网络系统的安全和管理技术；6) 掌握代码编译的基本原理和技术。',
      '（2）掌握面向实践应用的基本理论、方法和工具，包括：1) 掌握功能抽象和数据抽象的基本技术，以及构造程序设计中基本数据单元的方法和相关评价标准；2) 掌握若干基本的算法设计技术和算法评价标准；3) 掌握现代高级语言的高级语法机制和高级编程技术；4) 掌握结构化方法和面向对象方法的基本原理和相关编程技术；5) 掌握Web应用技术的基本原理，以及面向Web的编程技术；6) 掌握大型软件的分析、设计、编码、测试、维护和评估的基本技术，了解项目工程化开发流程、准则、标准和规范，了解项目管理和质量保证的基本方法；7) 掌握数据库基本原理、性能分析技术、开发语言和应用技术。',
      '应用知识能力：综合运用所学理论、技术方法和手段，学会发现问题、分析问题并解决问题,包括：1) 从实践中发现问题、了解问题；2) 定义问题的相关因素、进行定性分析，并提炼问题；3) 建立模型，采用理论分析、实验等手段进行具体分析；4) 提出解决方法和建议。工程实践能力：1) 掌握解决工程问题的先进技术、方法和管理手段；2) 能从事软件项目的设计、施工、管理，适应多学科交叉复合的新工业领域应用中对计算机专业应用的各种需要，满足区域经济发展中对计算机专业应用的各种应用需要。开拓创新能力：1) 具有较强的创新意识和进行软件项目设计、技术改造与创新的基本能力；2) 具有一定的计算思维能力和系统驾驭能力；3) 具有组织、协调和开展软件工程项目的基本能力。',
      '（1）科学素养：1) 具有严谨求实的科学精神；2) 具有面向未来，开拓进取的开创精神；3) 具有针对工程问题特点的科学思维方式。（2）工程素养：1) 具备对个人和集体目标、团队利益负责的职业精神；2) 能够通过持续不断的学习，找到解决问题的新方法，具有对新技术的推广或对现有技术进行革新的进取精神；3) 具有在前瞻未来、承担责任、规划前景、坚持原则、灵活处理工作和团队合作时，面对挑战和挫折的乐观主义精神；4) 坚持原则，具有勇于承担责任、为人诚实、正直的道德准则。具有良好的市场、质量和安全意识，注重环境保护、生态平衡和可持续发展的社会责任感。'
    ]
  }
]

const directionParagraphs = [
  '本专业培养的毕业生德、智、体、美全面发展，具有良好的科学素养、文化修养和创新创业精神，以及良好的创新精神和团队协作精神，系统地掌握本专业的基础理论、基本方法和基本技能，具备应用计算机科学软硬件及网络知识解决实际问题的能力；受过科学研究与实际应用的初步训练，能够适应学科的新发展，有较强的程序设计与计算思维能力，具有一定的创新能力。本专业毕业生可在政府、企事业单位从事计算机相关的应用、管理和开发工作，也可在金融、通信等信息技术企事业单位从事信息系统、网络系统的开发和运维工作。主要课程包括：高等数学、大学物理、线性代数及应用、离散数学、概率统计、信息技术导论、高级语言程序设计、数据结构、算法设计与分析、面向对象程序设计、软件工程导论、操作系统、编译原理与技术、数据库系统、计算机原理、计算机系统结构、计算机网络、Web应用技术、数字逻辑基础、英语、马克思主义原理、体育等。'
]
</script>

<template>
  <div class="programme-page">
    <div class="page-title">培养方案查询</div>

    <div class="toolbar-panel">
      <div class="toolbar-icon" aria-hidden="true"></div>
      <div class="toolbar-item">
        <span class="toolbar-label">年级:</span>
        <select v-model="selectedYear" class="toolbar-select">
          <option v-for="year in yearOptions" :key="year" :value="year">{{ year }}</option>
        </select>
      </div>
      <div class="toolbar-item major-item">
        <span class="toolbar-label">专业:</span>
        <select v-model="selectedMajor" class="toolbar-select major-select">
          <option v-for="major in majorOptions" :key="major" :value="major">{{ major }}</option>
        </select>
      </div>
      <button class="query-button" type="button" @click="submitQuery">查询</button>
    </div>

    <div class="programme-name">专业名称：{{ displayMajor }}</div>

    <table class="programme-table">
      <tbody>
        <tr>
          <th>培养目标:</th>
          <td>
            <p v-for="paragraph in objectiveParagraphs" :key="paragraph" class="content-paragraph">
              {{ paragraph }}
            </p>
          </td>
        </tr>
        <tr>
          <th>规格要求:</th>
          <td>
            <div v-for="block in requirementBlocks" :key="block.title" class="content-block">
              <div class="section-title">{{ block.title }}</div>
              <p v-for="paragraph in block.paragraphs" :key="paragraph" class="content-paragraph">
                {{ paragraph }}
              </p>
            </div>
          </td>
        </tr>
        <tr>
          <th>方向介绍:</th>
          <td>
            <p v-for="paragraph in directionParagraphs" :key="paragraph" class="content-paragraph">
              {{ paragraph }}
            </p>
          </td>
        </tr>
        <tr>
          <th>隶属科类:</th>
          <td>计算机类</td>
        </tr>
        <tr>
          <th>主干学科:</th>
          <td>计算机科学与技术</td>
        </tr>
        <tr>
          <th>相近专业:</th>
          <td>软件工程、网络工程、物联网、电子、通信、信息科学</td>
        </tr>
        <tr>
          <th>学历:</th>
          <td class="metric-cell">
            <div class="metrics-grid">
              <div class="metric-label">学历：</div>
              <div class="metric-value">本科</div>
              <div class="metric-label">标准学制:</div>
              <div class="metric-value">{{ displayYear === '2026年' ? '4 年' : '4 年' }}</div>
              <div class="metric-label">授予学位（学士）:</div>
              <div class="metric-value">工学</div>
              <div class="metric-label">毕业最低学分:</div>
              <div class="metric-value">170</div>
            </div>
          </td>
        </tr>
        <tr>
          <th>培养大纲:</th>
          <td>
            <a class="outline-link" href="javascript:void(0)">【查看培养大纲】</a>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.programme-page {
  min-height: 100%;
  padding: 4px 0 0;
  background: #fff;
  color: #222;
  font-family: SimSun, 'Songti SC', serif;
}

.page-title {
  margin-bottom: 2px;
  text-align: center;
  font-size: 18px;
  font-weight: 700;
  color: #2f2f2f;
}

.toolbar-panel {
  display: flex;
  align-items: center;
  gap: 8px;
  min-height: 38px;
  padding: 4px 10px;
  border: 1px solid #8ca8cf;
  background: linear-gradient(180deg, #8ec4ff 0%, #2d6ce3 60%, #6ab0ff 100%);
}

.toolbar-icon {
  position: relative;
  width: 22px;
  height: 22px;
  border-radius: 50%;
  flex: 0 0 auto;
  background: radial-gradient(circle at 35% 35%, #b7f0ff 0%, #2d8bf0 45%, #0c3a8e 100%);
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.7);
}

.toolbar-icon::after {
  content: '';
  position: absolute;
  right: -3px;
  bottom: -2px;
  width: 7px;
  height: 7px;
  border: 2px solid #356027;
  border-radius: 50%;
}

.toolbar-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.major-item {
  margin-left: auto;
}

.toolbar-label {
  color: #fffef4;
  font-size: 14px;
  font-weight: 700;
}

.toolbar-select,
.query-button {
  height: 28px;
  border: 1px solid #6a6a6a;
  font-size: 14px;
  font-family: SimSun, 'Songti SC', serif;
}

.toolbar-select {
  min-width: 114px;
  padding: 0 8px;
  background: #fff;
}

.major-select {
  min-width: 350px;
}

.query-button {
  min-width: 52px;
  padding: 0 10px;
  background: linear-gradient(180deg, #fff8d7 0%, #ece4bf 100%);
  cursor: pointer;
}

.programme-name {
  padding: 6px 2px 4px;
  font-size: 15px;
}

.programme-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.programme-table th,
.programme-table td {
  border: 1px solid #b8b8b8;
  vertical-align: top;
}

.programme-table th {
  width: 128px;
  padding: 14px 10px;
  text-align: center;
  vertical-align: middle;
  font-size: 14px;
  font-weight: 700;
  white-space: nowrap;
}

.programme-table td {
  padding: 10px 12px;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-all;
}

.content-block + .content-block {
  margin-top: 8px;
}

.section-title {
  margin-bottom: 4px;
  font-size: 14px;
  font-weight: 700;
}

.content-paragraph {
  margin: 0;
  text-indent: 1em;
}

.metric-cell {
  padding: 0 !important;
}

.metrics-grid {
  display: grid;
  grid-template-columns: 90px 1fr 110px 1fr 190px 1fr 140px 1fr;
}

.metric-label,
.metric-value {
  min-height: 42px;
  display: flex;
  align-items: center;
  padding: 0 10px;
}

.metric-label {
  justify-content: center;
  border-right: 1px solid #b8b8b8;
  font-weight: 700;
}

.metric-value {
  border-right: 1px solid #b8b8b8;
}

.metrics-grid > :last-child {
  border-right: 0;
}

.outline-link {
  color: #1e199f;
  font-weight: 700;
  text-decoration: underline;
}

@media (max-width: 900px) {
  .programme-page {
    overflow-x: auto;
  }

  .toolbar-panel,
  .programme-table {
    min-width: 920px;
  }
}
</style>
