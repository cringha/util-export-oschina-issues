## OSCHINA ISSUES 导出工具  ##

OSCHINA-ISSUES导出工具，是一个方便导出指定用户、项目ISSUES 的小工具。 它会下载的每个ISSUE 
内容，包含图片，以JISON格式保存在本地路径，可以方便导入其他的 BUG 跟踪系统。
 


### 安装 : ###

在Window平台，执行publish.bat脚本，会复制所需文件到 dist目录

    cd .
    publish.bat
    cd dist
    run.bat 


### 使用说明：###
  

**参数**：
 
>* --login account     **OSCHINA 的登录账号**
>* --pass  password    **命令行输入口令，如果不从命令行输入，控制台会交互式提示输入口令**
>* --user  issue's owner  **需要下载的 ISSUE 所属账号（ISSUE_OWNER）,如果 此项为空，那么 采用 login的用户**
>* --proxy  proxy_address:port  -**网络代理，如果需要的话**
>* --project  project name    **issue 的项目**
>* --from   from issue begin, **default 1  -从那个issue开始下载**
>* --to    to issue end   **下载到那个issue结束**
>* --base  output base dir, default ./   **保存路径，默认保存在当前目录**
>* --help  help info  **当前显示的信息**



**OSCHINA ISSUE URL**
>       

    http://git.oschina.net/<ISSUE_OWNER>/<PROJECT>/issues/<ISSUE-ID>

OSCHINA ISSUE URL 如上所示,

* ISSUE_OWNER ： OSCHINA 登录账号
* PROJECT ： 所属项目名称
* ISSUE-ID : ISSUEID，数字，从1开始 


### 使用 示例 ###


下载 login 的 project 的 从1到100的ISSUE

    run --login [你的登录账号] --project [你要下载的项目名称] --from 1 --to 100
 

下载 login 的 project 的 从1到100的ISSUE ，使用 proxy 代理服务器

    run --login [你的登录账号] --project [你要下载的项目名称] --from 1 --to 100  --proxy [你的代理服务器]

下载  User 的 project 的 从1到100的ISSUE

    run --login [你的登录账号] --project [你要下载的项目名称] --from 1 --to 100  --user [其他的账号]
  



 

 
