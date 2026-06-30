<template>
  <div class="org-page">
    <div class="breadcrumb">当前位置：机构职能</div>

    <details open>
      <summary>处领导</summary>
      <div class="section-content">
        <div class="person">
          <p><strong>处长：</strong>揭安全 教授 全面主持工作</p>
          <p>联系电话：0791-88120275</p>
        </div>
        <div class="person">
          <p><strong>教育教学评估中心主任：</strong>徐华银</p>
          <p>全面主持教育教学评估中心工作，协助处长分管教学信息科、教学质量科</p>
          <p>联系电话：0791-88120276</p>
        </div>
        <div class="person">
          <p><strong>副处长：</strong>蒋长根 副教授</p>
          <p>协助处长分管教学研究科、教学建设科</p>
          <p>联系电话：0791-88120278</p>
        </div>
        <div class="person">
          <p><strong>副处长：</strong>吴传孙 副教授</p>
          <p>协助处长分管教学管理科、联合培养科、实践教学科</p>
          <p>联系电话：0791-88120268</p>
        </div>
        <div class="person">
          <p><strong>学生学习指导中心主任：</strong>金绍梅</p>
          <p>联系电话：0791-88120272</p>
          <p>协助处长分管综合办公室、语言文字工作办公室、学习指导科</p>
        </div>
        <div class="person">
          <p><strong>教育教学评估中心副主任：</strong>胡小燕</p>
          <p>联系电话：0791-88122382</p>
          <p>协助教育教学评估中心主任分管教育督导科、评估监测科、教学质量科</p>
        </div>
        <div class="person">
          <p><strong>文化艺术教学部专职副主任：</strong>廖序 副教授</p>
          <p>联系电话：0791-88132962</p>
          <p>负责文化艺术教学部日常工作，统筹推进文化艺术教育</p>
        </div>
      </div>
    </details>

    <details open>
      <summary>综合办公室</summary>
      <div class="section-content">
        <div class="person">
          <p>主任：程婷</p>
          <p>联系电话：0791-88120270</p>
          <p>职能：负责综合协调相关工作，包括文件收发、印章使用、经费和资产管理、综治工作等</p>
        </div>
      </div>
    </details>

    <details open>
      <summary>教学管理科</summary>
      <div class="section-content">
        <div class="person">
          <p>科长：黄伟</p>
          <p>联系电话：0791-88120273</p>
          <p>职能：负责教学管理相关工作，包括选课排课、考试管理、教室安排、教学工作量核算等</p>
        </div>
      </div>
    </details>

    <details open>
      <summary>联合培养科</summary>
      <div class="section-content">
        <div class="person">
          <p>科长：陈淑芳</p>
          <p>联系电话：0791-88120271</p>
          <p>职能：负责国内外联合办学、跨校选课和课程管理等</p>
        </div>
      </div>
    </details>

    <details open>
      <summary>教学信息科</summary>
      <div class="section-content">
        <div class="person">
          <p>科长：杨蕾</p>
        </div>
      </div>
    </details>
  </div>
</template>

<style scoped>
.org-page {
  width: min(100%, 1000px);
  color: #222;
  font-family: "Microsoft YaHei", sans-serif;
  font-size: 16px;
  line-height: 1.65;
}

.breadcrumb {
  padding-bottom: 4px;
  border-bottom: 1px dotted #ccc;
}

details {
  margin: 8px 0 18px 105px;
}

summary {
  padding: 0 5px 2px;
  border-bottom: 2px solid #999;
  cursor: pointer;
  font-size: 18px;
  font-weight: 700;
}

.section-content {
  padding-left: 68px;
}

.person {
  padding: 12px 0;
  border-bottom: 1px dotted #aaa;
}

p {
  margin: 0;
}

@media (max-width: 760px) {
  details {
    margin-left: 0;
  }

  .section-content {
    padding-left: 20px;
  }
}
</style>
