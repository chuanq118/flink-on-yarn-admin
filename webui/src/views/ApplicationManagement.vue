<template>
  <el-row class="flink-yarn-table-controller">
    <el-radio-group v-model="SelectedState" size="large">
      <el-radio-button label="all">全部任务</el-radio-button>
      <el-radio-button label="finished">已完成</el-radio-button>
      <el-radio-button label="waiting">排队中</el-radio-button>
      <el-radio-button label="running">运行中</el-radio-button>
      <el-radio-button label="exception">异常任务</el-radio-button>
    </el-radio-group>
  </el-row>
  <el-row id="flink-yarn-table-block" align="middle" justify="center">
    <el-table :data="CurrentFlinkYarnApplications">
      <el-table-column prop="id" label="ID" width="60"/>
      <el-table-column prop="name" label="任务名"/>
      <el-table-column prop="host" label="主机名"/>
      <el-table-column prop="type" label="任务类型"/>
      <el-table-column prop="queue" label="队列" width="140"/>
      <el-table-column prop="user" label="用户" width="140"/>
      <el-table-column prop="state" label="运行状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.stateType" plain>{{ scope.row.state }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="trackingUrl" label="网页监控">
        <template #default="scope">
          <el-link class="my-el-link" @click="handleOpenUI(scope.row.trackingUrl)">
            <span><el-icon><Link/></el-icon></span>
            <span v-if="scope.row.state === 'RUNNING'">Monitor&nbsp;UI</span>
            <span v-else>History&nbsp;UI</span>
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="trackingUrl" label="操作">
        <template #default="scope">
          <el-button @click="handleCancelJob(scope.row)" circle :disabled="scope.row.state !== 'RUNNING'">STOP</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-row>


</template>

<script lang="ts" setup>

import {onMounted, ref, watch} from "vue"
import {cancelFlinkYarnApplication, listFlinkYarnApplications} from "@/api"
import type {YarnApplication} from "@/domain"
import {handlePageResize, ReplaceHostnameWithOutboundIp} from "@/utils"
import {ElMessage} from "element-plus"

import {Link} from "@element-plus/icons-vue"

const AllFlinkYarnApplications = ref<Array<YarnApplication>>([])
const CurrentFlinkYarnApplications = ref<Array<YarnApplication>>([])
const SelectedState = ref<"all" | "finished" | "waiting" | "running" | "exception">("all")

onMounted(async () => {
  handlePageResize()
  let data = await listFlinkYarnApplications()
  if (data != null) {
    AllFlinkYarnApplications.value = []
    for (let app of data) {
      handleAppStateType(app)
      handleAppTrackingUrl(app)
      AllFlinkYarnApplications.value?.push(app)
    }
    CurrentFlinkYarnApplications.value = AllFlinkYarnApplications.value
  }
})

watch(SelectedState, async (val)=>{
  CurrentFlinkYarnApplications.value = []
  if (val == "all") {
    CurrentFlinkYarnApplications.value = AllFlinkYarnApplications.value
  }else if (val == "finished") {
    for (let app of AllFlinkYarnApplications.value) {
      if (app.state == "FINISHED") {
        CurrentFlinkYarnApplications.value.push(app)
      }
    }
  }else if (val == "running") {
    for (let app of AllFlinkYarnApplications.value) {
      if (app.state == "RUNNING") {
        CurrentFlinkYarnApplications.value.push(app)
      }
    }
  }else if (val == "exception") {
    for (let app of AllFlinkYarnApplications.value) {
      if (app.state == "FAILED" || app.state == "KILLED") {
        CurrentFlinkYarnApplications.value.push(app)
      }
    }
  }else if (val == "waiting") {
    for (let app of AllFlinkYarnApplications.value) {
      if (app.state == "ACCEPTED" || app.state == "SUBMITTED" || app.state == "NEW_SAVING" || app.state == "NEW") {
        CurrentFlinkYarnApplications.value.push(app)
      }
    }
  }
})

function handleAppStateType(app: YarnApplication) :void{
  if (app.state == "FAILED" || app.state == "KILLED") {
    app.stateType = "danger"
    return
  }
  if (app.state == "RUNNING") {
    app.stateType = "success"
    return
  }
  if (app.state == "ACCEPTED") {
    app.stateType = "warning"
    return
  }
  app.stateType = "info"
}

function handleAppTrackingUrl(app: YarnApplication): void {
  // const url = new URL(app.trackingUrl)
  // app.trackingUrl = url.protocol + "//" + "host:port" + url.pathname
}

async function handleCancelJob(app :YarnApplication){
  const result = await cancelFlinkYarnApplication(app.timestamp, app.id)
  if (result === true) {
    ElMessage({
      type: "success",
      message: "成功结束任务"
    })
    app.state = "KILLED"
  }else {
    ElMessage({
      type: "warning",
      message: result
    })
  }

}

function handleOpenUI(url: string) {
  window.open(ReplaceHostnameWithOutboundIp(url))
}

</script>
