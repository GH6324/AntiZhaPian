# 最新变化
软件新版本似乎使用了腾讯的新加固，代码里出现了大量的vmppro.init，几乎把所有有用函数的实现
都隐藏了，本人没系统研究过安卓的去加固，而且因为这个加固方式是新出现的，全网好像也没找到
解决的办法，所以新版本的很多实现都只能猜，没办法看到它的实现过程，很多事情都做不了了。
新版本的通讯使用了ECHD密钥协商，大部分和服务器的通讯都使用协商密钥进行了加密，而且每次通
讯都有个seqid序列号，这个序列号在密钥协商时从服务器获得，每次通讯的时候进行计算迭代，比
如a=(a*13+77)%65535，这个迭代过程被上面所说的加密隐藏了，无法找到具体的迭代算法，而且结果
数据也被ECHD加密了，理论上只通过网络数据拦截是无法解密出每次的seqid从而猜测迭代过程，所以
只要服务器校验了seqid，我们就会被发现。
所以通过这两步处理，似乎我们无法再假装正版，进而获得更新资料了。

# AntiZhaPian
伪·国家反诈中心  

最原始的代码来自 https://github.com/XJP-GIT/AntiZhaPian  
https://github.com/zhanghua000/AntiZhaPian 的作者增加了一些自定义属性  
最后我反编译了官方包，使用官方程序的布局，尽量保证各个页面的显示效果和官方一致  
本人不会安卓编程，也不懂kotlin语法，完全是瞎蒙，所以有问题和建议尽管提，但是不保证会改。  

# [更新日志](https://github.com/newhying/AntiZhaPian/blob/main/CHANGELOG.md)  

# 使用方法  
点击```我的---点击查看个人信息 >```即可查看和更新个人信息  
设置名字后，```我的```页的多余的信息就会隐藏  

# 注意事项  
为了和官方APP更像，本程序会尽量使用官方的数据，但是如果官方在提供这些数据的时候必须登录，  
那么我们就不可能始终保持最新，所以，那些依赖网络数据的功能可能就无法保证一直和官方一致，  
如果要更新这些数据，可以找到官方程序登陆后保存的note_national.xml文件，然后放到本程序指定  
的位置（具体哪找文件，放哪，自己想办法和看代码，如果你都不会，那也就不适合做这些操作），  
然后启动程序，在关于页，点击获取更新按钮，就可以更新资料，如果资料格式有变化，可能导致程  
序无法正常使用。当然本功能也不能保证一直能用。  
使用某些功能会需要某些权限，比如媒体和应用统计，打开后可以通过假的实名认证页关闭，  
关闭的时候会打开设置页，由用户手动关闭（用户可以自己在系统设置中关闭），但是关闭一些权限  
会导致程序重启，所以可能需要多次在认证页点击关闭按钮，直到提示已经全部关闭即可。  
本程序不包含任何实用功能，比如病毒扫描、诈骗举报等，如果需要那些功能，就安装官方包吧。  
即使不装官方程序，你的电话等也是会被监控的，比如接了某些电话就可能会被96110回访，所以安装  
本程序也只是心理安慰，防止你手机上装的一些APP，比如翻墙VPN被官方程序上报。但是如果微信什  
么的偷偷上报，你也不知道是不是。
