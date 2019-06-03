# FinalExam
- 部署服务器：http://172.22.161.59:8080/login
## 接口的路由

### 登录接口
- /login： 用于加载登录的html界面
- /check    检查登录注册时用户名是否存在，登录时密码是否正确。
- /register 用于加载注册的html界面

### 游戏房间以及大厅的接口

- /play 用于加载游戏大厅的html界面
- /room 用于加载游戏房间的html界面
- /createRoom 用于创建游戏房间。参数包括 房主名(host)以及房间密码(password)
- /checkRoom 用于非房主的用于加入房间时房间密码的验证，参数：房主名(host),
- /ready 在游戏开始前的用户准备状态的改变。
- /clear 在游戏结束，或者所有用户离开游戏房间时，清理棋盘，以及房间的用户信息
- /regret 悔棋的接口，用户可以通过这个接口悔棋

- /readyNum 查看在该房间里面准备的用户有多少个，如果有两个了，则开始游戏

- /checkPassword 检查这个放假是否有密码，如果没有密码则可以直接进入。

## 非基础功能的实现流程
### 房间密码
- 在第一个用户点击创建房间时，就成为房主，同时弹出对话框，决定是否为这个房间创建密码。如果房主输入了密码，通过createRoom这个接口传输到服务端。后台通过redis进行存储。
- 其他用户在点击加入房间按钮时候，首先前端通过ajax请求后台，查看房间是否有密码。如果有，则需要验证身份。如果没有，则直接进入。

### 房间聊天以及游戏中聊天
- 用户在进入游戏房间之后，用户名会被传输到后台，然后使用redis进行存储用户名。在聊天时，首先去redis取到用户名单。然后再去总的在线人数的map（这个map存储了用户名->websocket的session）中取到session。通过session进行通信。

### 房间观战
- 每个游戏房间可以多个用户进入，当两个人点了准备按钮之后，准备按钮消失，这个房间的其他用户就不能准备，但是可以接收到系统发来的对战信息。

### 悔棋
- 当用户点击悔棋按钮时，首先通过ajax向后台发送请求，找到刚刚用户所走的那步棋的坐标，然后把棋盘数组的对应位置的数据清零。当清零完成后，后台向前端发送清除完成的信息。该用户可以继续完成这一步棋。

### 认输
- 用户点击了认输按钮之后，前端向后台发送用户认输信息，然后后台将收到信息返回给前端。交战双方都会收到该信息弹窗，当关闭弹窗之后，交战双方返回游戏大厅，同时将棋盘数组清零

### 求和
- 用户点击求和按钮之后，弹出对话框，如果双方都同意求和了之后，交战双方返回大厅。

### 代码运行环境
- Java-11.0.1
- mysql + redis
- sql文件再resource中
