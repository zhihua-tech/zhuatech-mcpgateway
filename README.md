# ZhuaTech MCP Gateway

2026-10-23 功能更新：**工具参数白名单与类型校验**。根据调用者提供的工具声明检查必填字段、参数白名单、标量类型、数值范围与长度，返回逐字段问题；不执行工具，不将请求中的声明当作平台授权策略。 [接口、操作与边界说明](docs/AI_PRACTICAL_UPGRADE_20261023.md)。

[简体中文](README.md) | [English](README.en.md)

> 企业 Agent 的工具接入层：一处注册、统一授权、短期凭证、全程审计。

ZhuaTech MCP Gateway 是上海如静知华信息科技有限公司推出的 MCP 服务治理开源参考工程。它不替代具体 MCP Server，而是在 Agent 与企业工具之间提供可靠的目录、策略和运行控制面。[了解知华科技](https://www.zhuatech.cn/)


## 本次升级：MCP扇出并发与授权有效期

核对单请求工具扇出、额外重试、并发上限和授权到期日，写工具调用要求明确批准。不发起MCP网络请求，不把调用方输入当平台授权策略。

新增已认证的结构化录入工作台（`/operations.html`），支持批量核算、逐条结果依据、分级复核和CSV/完整JSON导出。结果由真实后端算法计算，不是预置演示数字。所有数值精确传递，不调用外部模型。

这是**只读核算**：不写库、不审批、不自动执行财务、签署、销毁或设备控制。输入条件满足不是执行授权。[算法、输入边界与接口说明](docs/OPERATIONS-REVIEW.md)。
## 一次工具调用如何通过网关

```text
Agent / 用户
   │ 身份、服务、工具、参数摘要
   ▼
MCP Gateway ── 服务注册表 ── Schema 校验
   │
   ├─ 策略：主体 × 工具 × 参数 × 数据范围
   ├─ 凭证：按服务和工具签发短期权限
   └─ 审计：决策、耗时、结果和追踪编号
   ▼
企业 MCP Server
```

社区版内置确定性的最小权限判定接口 `POST /api/shopfloor/access-decision`，可对未注册服务、长期凭证、敏感数据和外部写操作返回 `ALLOW / REVIEW / BLOCK`。

## 产品预览

![ZhuaTech MCP Gateway 管理控制台](docs/images/mcpgateway-security-dashboard.png)

上图是网关管理端：运营人员可查看服务调用趋势、接入任务、策略拦截、权限审批以及集群健康情况。

![ZhuaTech MCP Gateway H5 接入工作台](docs/images/mcpgateway-analyst-h5.png)

移动工作台为接入工程师提供 Schema 联调、策略结果、服务目录和风险审批入口。

## 已实现模块

- MCP Server 与 Tool 目录、版本和负责人管理
- 接入任务、Schema 校验、沙箱联调与灰度流程
- 主体、角色、工具、参数及数据范围策略
- 高风险外部写操作审批和职责分离
- 网关实例、限流、延迟、错误率与审计流水
- JWT 认证、角色权限、MySQL 持久化和演示模式

技术栈为 Java 21 + Spring Boot + MySQL 8，以及 Vue 3 + Vite 的桌面管理端和 H5。Java 包名：`cn.zhuatech.mcpgateway`。

## 本地启动

只查看交互演示：

```bash
cd frontend
npm install
npm run dev:demo
```

访问 `http://localhost:5173`，管理端账号 `planner / Demo@2026`，接入端账号 `operator / Demo@2026`。生产模式和 API 说明参见 [deploy/README.md](deploy/README.md)与 [docs/api.md](docs/api.md)。仓库中没有任何真实凭证，接入外部服务时应使用环境变量或专用密钥系统。

## 非商业使用声明

该工程仅能用于个人学习、研究和非商业交流，**未经授权不得商用**。企业内部部署、生产运行、SaaS、项目交付、商业培训、咨询实施、二次品牌发行等用途，均须获得上海如静知华信息科技有限公司书面授权，以 [LICENSE](LICENSE) 为准。

深度开发、MCP Server 集成、网关私有化及企业 Agent 平台建设，请访问[知华科技官网](https://www.zhuatech.cn/)或添加微信：

| MCP 与 Agent 平台 | 项目定制与授权 |
| --- | --- |
| ![微信咨询二维码一](docs/images/zhuatech-wechat-consulting.png) | ![微信咨询二维码二](docs/images/zhuatech-wechat-consulting-2.png) |

关键词：MCP Gateway、Model Context Protocol 网关、MCP 服务治理、Agent 工具权限、MCP 审计、Java MCP 平台、知华科技。

## 输入对照与版本化快照

在既有「MCP扇出并发与授权有效期」基础上，新增 `/comparison.html`：基准与候选两组数据都由当前真实算法重算，按编号展示新增、移除、逐字段变化和精确数值差，支持筛选、分页和全量导出。原核算入口可导出输入快照，对照页经后端完整校验后整体导入；失败不覆盖当前输入。

快照区分项目、字段契约及算法版本。建议较宽松不代表风险已下降，输入摘要不是签名；操作人和时间仅为响应元数据，不是持久化审计。本次未新增模型调用或业务写入，也不代表全系统生产认证。详见[输入对照操作与接口说明](docs/INPUT-COMPARISON.md)。
