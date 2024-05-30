<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<el-select v-model="query.state" placeholder="状态" class="handle-select mr10">
          <el-option key="-1" label="不限" value=""> 不限 </el-option>
          <el-option key=0 label="已激活" value=0> 已激活 </el-option>
          <el-option key=1 label="未激活" value=1> 未激活 </el-option>
					<el-option key=2 label="已停用" value=2> 已停用 </el-option>
				</el-select>
				<el-input v-model="query.activationCode" placeholder="激活码" class="handle-input mr10"></el-input>
				<el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
				<el-button type="primary" :icon="Plus" @click="handleAdd"> 添加 </el-button>
			</div>
			<el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
<!--				<el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>-->
				<el-table-column prop="activationCode" label="激活码" align="center" width="160"></el-table-column>
				<el-table-column prop="activateTime" label="激活时间" align="center"></el-table-column>
				<el-table-column prop="deactivateTime" label="失效时间" align="center"></el-table-column>
        <el-table-column prop="effectiveTime" label="剩余时长" align="center" width="100"></el-table-column>
				<el-table-column label="状态" align="center" width="150">
					<template #default="scope">
						<el-tag
							:type="scope.row.state === 0 ? 'success' : scope.row.state === 1 ? '' : 'danger'"
						>
							{{ scope.row.state === 0 ? '已激活':scope.row.state === 1 ? '未激活':'停用' }}
						</el-tag>
					</template>
				</el-table-column>

				<el-table-column label="操作" width="220" align="center">
					<template #default="scope">
						<el-button text :icon="Edit" @click="handleEdit(scope.$index, scope.row)" v-permiss="15">
							编辑
						</el-button>
<!--						<el-button text :icon="Delete" class="red" @click="handleDelete(scope.$index, scope.row)" v-permiss="16">-->
<!--							删除-->
<!--						</el-button>-->
					</template>
				</el-table-column>
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

		<!-- 编辑弹出框 -->
		<el-dialog title="编辑" v-model="editVisible" width="30%">
			<el-form label-width="70px">
				<el-form-item label="激活码">
					<el-input v-model="form.activationCode"></el-input>
				</el-form-item>
				<el-form-item label="激活时间">
					<el-input v-model="form.activateTime"></el-input>
				</el-form-item>
        <el-form-item label="失效时间">
          <el-input v-model="form.deactivateTime"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="region">
          <el-select v-model="form.state" placeholder="请选择">
            <el-option key=0 label="已激活" value=0> 已激活 </el-option>
            <el-option key=1 label="未激活" value=1> 未激活 </el-option>
            <el-option key=2 label="已停用" value=2> 已停用 </el-option>
          </el-select>
        </el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="editVisible = false">取 消</el-button>
					<el-button type="primary" @click="saveEdit">确 定</el-button>
				</span>
			</template>
		</el-dialog>

<!--    添加 弹出框-->
    <el-dialog title="添加" v-model="addVisible" width="30%">
      <el-form label-width="70px">
        <el-form-item label="数量">
          <el-input v-model="addForm.num"></el-input>
        </el-form-item>
        <el-form-item label="有效期">
          <el-input v-model="addForm.effectiveTime"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="addVisible = false">取 消</el-button>
					<el-button type="primary" @click="submitAdd">确 定</el-button>
				</span>
      </template>
    </el-dialog>
	</div>
</template>

<script setup lang="ts" name="activate">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete, Edit, Search, Plus } from '@element-plus/icons-vue';
import {addActivation, deleteActivation, fetchActivation, fetchData, updateActivation} from '../api/index';
import axios from "axios";

interface TableItem {
  activationCode: string
  activateTime: string
  deactivateTime : string ;
  effectiveTime : number;
  state : number;
}

const query = reactive({
  activationCode: null,
  state: null,
	pageIndex: 1,
	pageSize: 10
});

const tableData = ref<TableItem[]>([]);
const pageTotal = ref(0);
// 获取表格数据
const getData = () => {
  fetchActivation(query).then(res => {
    tableData.value = res.data.data.records;
		pageTotal.value = res.data.data.total || 50;
	});
};
getData();

// 查询操作
const handleSearch = () => {
	query.pageIndex = 1;
	getData();
};
// 分页导航
const handlePageChange = (val: number) => {
	query.pageIndex = val;
	getData();
};

// 删除操作
const handleDelete = (index: number, row: any) => {
	// 二次确认删除
	ElMessageBox.confirm('确定要删除吗？', '提示', {
		type: 'warning'
	})
  .then(() => {
      deleteActivation({
        activationCode: row.activationCode
      }).then(resp => {
        ElMessage.success('删除成功');
        tableData.value.splice(index, 1);
      })

		}).catch(() => {});
};

// 添加激活码时弹出框
const addVisible = ref(false);
const addForm = reactive({
  num: 0,
  effectiveTime: 0,
});

const handleAdd = () => {
  addVisible.value=true
};

const submitAdd = () => {
  addActivation({
    num : addForm.num,
    effectiveTime : addForm.effectiveTime
  }).then(res =>{
    ElMessage.success(`添加成功`);
  })
  addVisible.value=true
}

// 表格编辑时弹窗和保存
const editVisible = ref(false);
const form = reactive({
  activationCode: '',
  activateTime: '',
  deactivateTime: '',
  state: 0,
});
let idx: number = -1;
const handleEdit = (index: number, row: any) => {
	idx = index;
	form.activationCode = row.activationCode;
	form.activateTime = row.activateTime;
  form.deactivateTime = row.deactivateTime;
  form.state = row.state.toString();
  editVisible.value = true;
};
const saveEdit = () => {
  updateActivation({
    activationCode: form.activationCode,
    deactivateTime: form.deactivateTime,
    state: form.state
  }).then(response =>{
    ElMessage.success(`修改第 ${idx + 1} 行成功`);
    tableData.value[idx].activationCode = form.activationCode;
    tableData.value[idx].activateTime = form.activateTime;
    tableData.value[idx].deactivateTime = form.deactivateTime;
    tableData.value[idx].state = Number(form.state);
    tableData.value[idx].effectiveTime = response.data.data;
  })
	editVisible.value = false;

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
