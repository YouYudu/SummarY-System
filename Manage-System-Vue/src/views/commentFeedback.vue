<template>
	<div>
		<div class="container">
<!--      <div class="handle-box">-->
<!--        <el-button type="primary" @click="exportXlsx">导出Excel</el-button>-->
<!--      </div>-->
			<el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
				<el-table-column prop="code" label="代码片段"></el-table-column>
				<el-table-column prop="comment" label="代码注释"></el-table-column>
				<el-table-column prop="accuracy" label="准确性" width="70" align="center"></el-table-column>
        <el-table-column prop="fluency" label="流畅性" width="70" align="center"></el-table-column>
				<el-table-column prop="informativeness" label="信息量" width="70" align="center"></el-table-column>
        <el-table-column prop="suggestion" label="用户评论"></el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination
					background
					layout="total, prev, pager, next"
					:current-page="query.pageIndex"
					:page-size="query.pageSize"
					:total="pageTotal"
					@current-change="handlePageChange"
				></el-pagination>
			</div>
		</div>

	</div>
</template>

<script setup lang="ts" name="basetable">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete, Edit, Search, Plus } from '@element-plus/icons-vue';
import {fetchCommentFeedback, fetchData} from '../api/index';
import axios from "axios";
import * as XLSX from "xlsx";

interface TableItem {
	id: number;
	code: string;
	comment: string;
	feedback: string;
	score1: number;
  score2: number;
  score3: number;
}

const query = reactive({
	pageIndex: 1,
	pageSize: 10
});
const tableData = ref<TableItem[]>([]);
const pageTotal = ref(0);
// 获取表格数据
const getData = () => {
  fetchCommentFeedback(query).then(res => {
    tableData.value = res.data.data.records;
    pageTotal.value = res.data.data.total || 50;
	});
};
getData();


// 分页导航
const handlePageChange = (val: number) => {
	query.pageIndex = val;
	getData();
};

const list = [['ID', '代码片段', '代码摘要', '准确性', '流畅性', '覆盖性', '用户评论']];
const exportXlsx = () => {
  tableData.value.map((item: any, i: number) => {
    const arr: any[] = [i + 1];
    arr.push(...[item.code, item.comment, item.score1, item.score2, item.score3, item.feedback]);
    list.push(arr);
  });
  let WorkSheet = XLSX.utils.aoa_to_sheet(list);
  let new_workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(new_workbook, WorkSheet, '第一页');
  XLSX.writeFile(new_workbook, `表格.xlsx`);
};


</script>



<style scoped>
.handle-box {
	margin-bottom: 20px;
}

.handle-select {
	width: 120px;
}

.handle-input {
	width: 300px;
}
.table {
	width: 100%;
	font-size: 14px;
}
.red {
	color: #F56C6C;
}
.mr10 {
	margin-right: 10px;
}
.table-td-thumb {
	display: block;
	margin: auto;
	width: 40px;
	height: 40px;
}
</style>
