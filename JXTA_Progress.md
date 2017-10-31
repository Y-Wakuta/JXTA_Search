# 2017/11/1 時点の進捗

* 双方向にクエリを送り合って、相手の接続しているDBからのクエリ結果を取得するところまでは成功
  (ただし、コンソールがシステムのログと同じになってしまっていて、ログが吐かれるとストリームのバッファが
   クリアされるっぽいから全力でSQLを書く必要あり)
* 現時点では多ノードでの通信を試みているところ

## 2017/10/25 時点の進捗

* 片方のピアからSQLクエリを投げてもう片方のピアのデータベースから結果を取得するのは成功。
* ただ、両方からやるのはまだ試せていないのとSleep時間の調整をしないと自由なタイミングでクエリを投げれるとは言えなそう
* JXTAのメソッド名だけをみてするのはそろそろ厳しいところもあるので、P2Pの勉強も含めて
Practical JXTAを読んでいこうかと

## 課題

* 自由なタイミングで相互にsqlを投げて結果を取得するようにする

## 現状

jdk9では無理
sqlを片方から投げて結果を取得するのは成功

## 2017/10/18 時点の進捗
* jdk9以降では動作しない様子
* JDBC単体について、データベースへのアクセスはできてる。

## 課題
* sqlを片方のPeerから送って、受信側JDBC？でクエリ結果を送り返す。
* pipeは開きっぱなしでcloseしない。
* No peer...は無視でいい

## 2017/10/11 時点の進捗

* 2台のパソコンで相互通信を行えることを確認。コード内にIPベタ書きしてない方のConfigファイル確認したらマルチキャストって書いてたから、ベタ書きしてる方のIPを相手のIPに書き換えたら通信ができるようになった。
* 相互通信で、任意のテキストをお互いに送れることを確認。ただし、通信のタイムアウトとかの問題があるから、標準入出力からの入力等はまだ試せていない。
* No peer...は定義元に戻るだけでは確認できなかった。



# 今後のタスク

1. 実際に2台のパソコンで通信を行うことが出来るか。
2. 3台以上のパソコンで双方向通信が可能かどうか
3. 任意のテキストを送信できるか。

1,3はコードをちょっと変更してみて動作確認を行う。（やる前にコミット忘れないでやろう）
2これは少し複雑目なのでこのうちでは最後、これはまずはExampleの実行。
3はコンソールから入力して遅れるかとかみる

## 通信確率用ソースコードについて

9/27(水)の進捗ミーティングでご覧頂いた、接続確立の例についてですが、「No peers connected this rendezvous!」のダイアログが出ると報告させて頂きましたが、ダイアログが出ていても出力されたログを確認する限り相手を認識することには成功しているようでした。

# 単方向通信

## 使用したソースコード

* Dimitri Edge
* Chandra RendezVous

## 結果

単方向のメッセージ送信は成功しているようです。ただし、以前ミーティングでもご覧いただいた「No peers connected to this rendezvous!」のダイアログは表示されます。

### 表示されたダイアログ(一部)

<div style="text-align: center;"><img src="./ChandraGetMessage.JPG" alt="start" width=450></div>

# 双方向通信

## 使用したソースコード

* Quinisela Edge
* Adelaide RendezVous

お互いにHello,GoodByeを送りあうコードです。

## 結果

両方ユーザがそれぞれメッセージを受け取ることが出来ました。ただし、sendMessage()で以下のIOExceptionが出てしまう場合もあり、その時は、sendMessageの対象となる側ではメッセージを受信することはできませんでした。また、お互いにメッセージを送りあえている状況においても「No Peers connected this rendezvous!」のダイアログは出てしまうようです。

## 表示されたダイアログ(一部)

<div style="text-align: center;"><img src="./AdelaideGetMessage1.JPG" alt="start" width=450></div>
<div style="text-align: center;"><img src="./AdelaideGetMessage2.JPG" alt="start" width=450></div>
<div style="text-align: center;"><img src="./QuiniselaGetMessage1.JPG" alt="start" width=450></div>
<div style="text-align: center;"><img src="./QuiniselaGetMessage2.JPG" alt="start" width=450></div>
<div style="text-align: center;"><img src="./ErrorAfterGetMessage.JPG" alt="start" width=450></div>
<div style="text-align: center;"><img src="./NoPeer.JPG" alt="start" width=450></div>

# ログ

## 単方向通信

### Dimitri

``` log
run:
10 01, 2017 5:54:58 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 355 net.jxta.platform.NetworkManager.configure()
Created new configuration. mode = EDGE
10 01, 2017 5:55:05 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 404 net.jxta.platform.NetworkManager.startNetwork()
Starting JXTA Network! MODE = EDGE,  HOME = file:/C:/Users/rocko/Documents/Laboratory/NetBeans/Dimitri/./Edge%20Dimitri,%20Sending%20Messages
10 01, 2017 5:55:05 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 286 net.jxta.peergroup.WorldPeerGroupFactory.newWorldPeerGroup()
Making a new World Peer Group instance using : net.jxta.impl.peergroup.Platform
10 01, 2017 5:55:05 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 308 net.jxta.impl.cm.Srdi.clearSrdi()
Clearing SRDI for null
10 01, 2017 5:55:05 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 129 net.jxta.impl.cm.InMemorySrdi.clearSrdi()
Clearing SRDIs for urn:jxta:jxta-WorldGroup[0,194494468] / [2088051243]
10 01, 2017 5:55:05 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Endpoint service (net.jxta.impl.endpoint.EndpointServiceImpl)
10 01, 2017 5:55:05 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Rendezvous Service (net.jxta.impl.rendezvous.RendezVousServiceImpl)
10 01, 2017 5:55:05 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Discovery service (net.jxta.impl.discovery.DiscoveryServiceImpl)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the HTTP Message Transport (net.jxta.impl.endpoint.servlethttp.ServletHttpTransport)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Peerinfo Service (net.jxta.impl.peer.PeerInfoServiceImpl)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Always Access Service (net.jxta.impl.access.always.AlwaysAccessService)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the TCP Message Transport (net.jxta.impl.endpoint.netty.NettyTransport)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Router Message Transport (net.jxta.impl.endpoint.router.EndpointRouter)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the IP Multicast Message Transport (net.jxta.impl.endpoint.mcast.McastTransport)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Resolver service (net.jxta.impl.resolver.ResolverServiceImpl)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : None Membership Service (net.jxta.impl.membership.none.NoneMembershipService)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 627 net.jxta.impl.endpoint.EndpointServiceImpl.startApp()
Endpoint Service started.
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 313 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 145 net.jxta.impl.endpoint.servlethttp.HttpMessageSender.start()
HTTP Client Transport started.
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 535 net.jxta.impl.endpoint.mcast.McastTransport.startApp()
IP Multicast Message Transport started.
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 327 net.jxta.impl.resolver.ResolverServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 141 net.jxta.impl.rendezvous.adhoc.AdhocPeerRdvService.<init>()
RendezVous Service is initialized for urn:jxta:jxta-WorldGroup as an ad hoc peer. 
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 365 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Rendezvous Service started
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1628 net.jxta.impl.discovery.DiscoveryServiceImpl.beEdge()
Already an Edge peer -- No Switch is needed.
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 547 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Discovery service started
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-WorldGroup[0,194494468] / [2088051243]] : Initialized routerSrdi
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-WorldGroup[0,194494468] / [2088051243]] : Starting SRDI GC Thread for routerSrdi
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 815 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
urn:jxta:jxta-WorldGroup[0,194494468] / [2088051243] : Router Message Transport started.
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 425 net.jxta.peergroup.NetPeerGroupFactory.newNetPeerGroup()
Instantiating net peer group : urn:jxta:jxta-NetGroup
	Parent : urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]
	ID : urn:jxta:jxta-NetGroup
	Name : NetPeerGroup
	impl : <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE jxta:MIA>
<jxta:MIA xml:space="default" xmlns:jxta="http://jxta.org">
	<MSID>
		urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000010206
	</MSID>
	<Desc>
		Default Network PeerGroup reference implementation
	</Desc>
	<Comp>
		<Efmt>
			JRE1.5
		</Efmt>
		<Bind>
			V2.0 Ref Impl
		</Bind>
	</Comp>
	<Code>
		net.jxta.impl.peergroup.ShadowPeerGroup
	</Code>
	<PURI>
		http://download.java.net/jxta/jxta-jxse/latest/jnlp/lib/jxta.jar
	</PURI>
	<Prov>
		sun.com
	</Prov>
	<Parm>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000080106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000060106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000040106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000030106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000070106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000100106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000020106
		</Svc>
		<Svc>
			urn:jxta:uuid-DDC5CA55578E4AB99A0AA81D2DC6EF3F3F7E9F18B5D84DD58D21CE9E37E19E6C06
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000050106
		</Svc>
		<Proto>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000F0106
		</Proto>
		<Proto>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000B0106
		</Proto>
	</Parm>
</jxta:MIA>

10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 308 net.jxta.impl.cm.Srdi.clearSrdi()
Clearing SRDI for null
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 129 net.jxta.impl.cm.InMemorySrdi.clearSrdi()
Clearing SRDIs for urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Relay Message Transport (net.jxta.impl.endpoint.relay.RelayTransport)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Endpoint service (net.jxta.impl.endpoint.EndpointServiceImpl)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Rendezvous Service (net.jxta.impl.rendezvous.RendezVousServiceImpl)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Pipe Service (net.jxta.impl.pipe.PipeServiceImpl)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Discovery service (net.jxta.impl.discovery.DiscoveryServiceImpl)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Peerinfo Service (net.jxta.impl.peer.PeerInfoServiceImpl)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Always Access Service (net.jxta.impl.access.always.AlwaysAccessService)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Router Message Transport (net.jxta.impl.endpoint.router.EndpointRouter)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Resolver service (net.jxta.impl.resolver.ResolverServiceImpl)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Content Service (net.jxta.impl.content.ContentServiceImpl)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : None Membership Service (net.jxta.impl.membership.none.NoneMembershipService)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 233 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is an endpoint service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 627 net.jxta.impl.endpoint.EndpointServiceImpl.startApp()
Endpoint Service started.
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 313 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 293 net.jxta.impl.pipe.PipeServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 327 net.jxta.impl.resolver.ResolverServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 243 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is a discovery service
EdgePeerRdvService: urn:jxta:jxta-NetGroup
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 220 net.jxta.impl.rendezvous.edge.EdgePeerRdvService.<init>()
RendezVous Service is initialized for urn:jxta:jxta-NetGroup as an Edge peer.
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 365 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Rendezvous Service started
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 293 net.jxta.impl.pipe.PipeServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 243 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is a discovery service
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]] : Initialized pipeResolverSrdi
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]] : Starting SRDI GC Thread for pipeResolverSrdi
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1628 net.jxta.impl.discovery.DiscoveryServiceImpl.beEdge()
Already an Edge peer -- No Switch is needed.
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 547 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Discovery service started
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]] : Initialized routerSrdi
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]] : Starting SRDI GC Thread for routerSrdi
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 136 net.jxta.impl.endpoint.netty.NettyTransportClient.getMessenger()
processing request to open connection to tcp://192.168.56.1:9722
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 815 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243] : Router Message Transport started.
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 215 net.jxta.impl.endpoint.relay.RelayClient.startClient()
Started client : relay://uuid-59616261646162614E50472050325033456467652044496DA974F2692C20536503
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 274 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Relay Message Transport started
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 314 net.jxta.impl.endpoint.relay.RelayClient.run()
Start relay client thread
10 01, 2017 5:55:06 午後 net.jxta.impl.endpoint.netty.NettyTransportClient getMessenger
情報: succeeded in connecting to tcp://192.168.56.1:9722, remote peer has logical address jxta://uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6403
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded module : Default Network PeerGroup reference implementation (net.jxta.impl.peergroup.ShadowPeerGroup)
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 424 net.jxta.platform.NetworkManager.startNetwork()
Started JXTA Network!
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 136 net.jxta.impl.endpoint.netty.NettyTransportClient.getMessenger()
processing request to open connection to tcp://10.1.0.4:9722
10 01, 2017 5:55:06 午後 net.jxta.impl.endpoint.netty.NettyTransportClient getMessenger
情報: succeeded in connecting to tcp://10.1.0.4:9722, remote peer has logical address jxta://uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6403
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 555 net.jxta.impl.rendezvous.edge.EdgePeerRdvService.addRdv()
New RDV lease from urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6403 C : -1506848106646 / -1506848106645
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE jxta:PipeAdvertisement>
<jxta:PipeAdvertisement xml:space="default" xmlns:jxta="http://jxta.org">
	<Id>
		urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6404
	</Id>
	<Type>
		JxtaUnicast
	</Type>
	<Name>
		Test Pipe
	</Name>
	<Desc>
		Created by RendezVous Chandra, Receiving Messages
	</Desc>
</jxta:PipeAdvertisement>

10 01, 2017 5:55:24 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 241 net.jxta.impl.pipe.NonBlockingOutputPipe.<init>()
Constructing for urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6404
10 01, 2017 5:55:24 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 675 net.jxta.impl.pipe.NonBlockingOutputPipe.startServiceThread()
Thread start : Worker Thread for NonBlockingOutputPipe : urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6404
	worker state : ACQUIREMESSENGER
10 01, 2017 5:55:25 午後 net.jxta.impl.util.threads.LongTaskDetector run
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] still running after 1,002ms in thread JxtaWorker-1, current stack:
java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
java.awt.WaitDispatchSupport.enter(WaitDispatchSupport.java:257)
java.awt.Dialog.show(Dialog.java:1084)
javax.swing.JOptionPane.showOptionDialog(JOptionPane.java:869)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:666)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:637)
Examples.Z_Tools_And_Others.Tools.PopInformationMessage(Tools.java:141)
Examples.G_Simple_Pipe_Communication.Edge_Dimitri_Sending_Messages.outputPipeEvent(Edge_Dimitri_Sending_Messages.java:74)
net.jxta.impl.pipe.PipeServiceImpl.pipeResolveEvent(PipeServiceImpl.java:770)
net.jxta.impl.pipe.PipeResolver.callListener(PipeResolver.java:1025)
net.jxta.impl.pipe.PipeResolver.processResponse(PipeResolver.java:727)
net.jxta.impl.resolver.ResolverServiceImpl.processResponse(ResolverServiceImpl.java:882)
net.jxta.impl.resolver.ResolverServiceImpl.access$900(ResolverServiceImpl.java:120)
net.jxta.impl.resolver.ResolverServiceImpl$DemuxResponse.processIncomingMessage(ResolverServiceImpl.java:1240)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.router.EndpointRouter.processIncomingMessage(EndpointRouter.java:1657)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.netty.NettyMessenger$1.run(NettyMessenger.java:148)
net.jxta.impl.util.threads.RunnableAsCallableWrapper.call(RunnableAsCallableWrapper.java:17)
net.jxta.impl.util.threads.RunMetricsWrapper.call(RunMetricsWrapper.java:50)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.call(QueueTimeRunMetricsWrapper.java:34)
net.jxta.impl.util.threads.RunMetricsWrapper.run(RunMetricsWrapper.java:93)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.run(QueueTimeRunMetricsWrapper.java:9)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
java.lang.Thread.run(Thread.java:745)

10 01, 2017 5:55:34 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 657 net.jxta.impl.pipe.NonBlockingOutputPipe.run()
Thread exit : Worker Thread for NonBlockingOutputPipe : urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6404
	worker state : SENDMESSAGES
10 01, 2017 5:55:36 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 675 net.jxta.impl.pipe.NonBlockingOutputPipe.startServiceThread()
Thread start : Worker Thread for NonBlockingOutputPipe : urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6404
	worker state : SENDMESSAGES
10 01, 2017 5:55:36 午後 net.jxta.impl.util.threads.RunMetricsWrapper call
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] took 11,968ms to complete in the shared executor
10 01, 2017 5:55:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 657 net.jxta.impl.pipe.NonBlockingOutputPipe.run()
Thread exit : Worker Thread for NonBlockingOutputPipe : urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6404
	worker state : SENDMESSAGES
10 01, 2017 5:56:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 5:57:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 5:58:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 5:59:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:00:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:01:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:02:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 483 net.jxta.platform.NetworkManager.stopNetwork()
Stopping JXTA Network!
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 264 net.jxta.impl.endpoint.relay.RelayClient.stopClient()
Stopped client : relay://uuid-59616261646162614E50472050325033456467652044496DA974F2692C20536503
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 302 net.jxta.impl.endpoint.relay.RelayTransport.stopApp()
Relay Message Transport stopped
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 845 net.jxta.impl.endpoint.router.EndpointRouter.stopApp()
urn:jxta:jxta-NetGroup "NetPeerGroup"[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243] : Router Message Transport stopped.
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 590 net.jxta.impl.discovery.DiscoveryServiceImpl.stopApp()
Discovery service stopped.
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 579 net.jxta.impl.rendezvous.edge.EdgePeerRdvService.removeRdv()
Disconnect from RDV urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6403
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 389 net.jxta.impl.rendezvous.RendezVousServiceImpl.stopApp()
Rendezvous Serivce stopped
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 686 net.jxta.impl.endpoint.EndpointServiceImpl.stopApp()
Endpoint Service stopped.
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 387 net.jxta.impl.endpoint.relay.RelayClient.run()
stop client thread
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 202 net.jxta.impl.cm.XIndiceIndexer.close()
Closing Indexer
10 01, 2017 6:03:03 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 501 net.jxta.platform.NetworkManager.stopNetwork()
Stopped JXTA Network!
10 01, 2017 6:03:06 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 1059 net.jxta.impl.endpoint.router.EndpointRouter.getGatewayAddress()
getGatewayAddress exception
java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask@280d4b26 rejected from net.jxta.impl.util.threads.SharedScheduledThreadPoolExecutor@6d0fc043[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 75]
Line 2047 java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution()
Line 823 java.util.concurrent.ThreadPoolExecutor.reject()
Line 326 java.util.concurrent.ScheduledThreadPoolExecutor.delayedExecute()
Line 573 java.util.concurrent.ScheduledThreadPoolExecutor.scheduleAtFixedRate()
Line 59 net.jxta.impl.util.threads.SharedScheduledThreadPoolExecutor.scheduleAtFixedRate()
Line 285 net.jxta.impl.endpoint.router.EndpointRouter$ClearPendingQuery.<init>()
Line 1011 net.jxta.impl.endpoint.router.EndpointRouter.getGatewayAddress()
Line 2412 net.jxta.impl.endpoint.router.EndpointRouter.addressMessage()
Line 169 net.jxta.impl.endpoint.router.RouterMessenger.sendMessageBImpl()
Line 789 net.jxta.impl.endpoint.BlockingMessenger.sendIt()
Line 742 net.jxta.impl.endpoint.BlockingMessenger.performDeferredAction()
Line 608 net.jxta.impl.endpoint.BlockingMessenger.sendMessageB()
Line 467 net.jxta.impl.endpoint.EndpointServiceImpl$CanonicalMessenger.sendMessageBImpl()
Line 490 net.jxta.endpoint.ThreadedMessenger.send()
Line 381 net.jxta.endpoint.ThreadedMessenger.run()
Line 745 java.lang.Thread.run()


```

### Chandra

``` log

run:
10 01, 2017 5:54:43 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 355 net.jxta.platform.NetworkManager.configure()
Created new configuration. mode = RENDEZVOUS
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 404 net.jxta.platform.NetworkManager.startNetwork()
Starting JXTA Network! MODE = RENDEZVOUS,  HOME = file:/C:/Users/rocko/Documents/Laboratory/NetBeans/Chandra/./RendezVous%20Chandra,%20Receiving%20Messages
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 286 net.jxta.peergroup.WorldPeerGroupFactory.newWorldPeerGroup()
Making a new World Peer Group instance using : net.jxta.impl.peergroup.Platform
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 308 net.jxta.impl.cm.Srdi.clearSrdi()
Clearing SRDI for null
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 129 net.jxta.impl.cm.InMemorySrdi.clearSrdi()
Clearing SRDIs for urn:jxta:jxta-WorldGroup[0,310656974] / [1537358694]
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Endpoint service (net.jxta.impl.endpoint.EndpointServiceImpl)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Rendezvous Service (net.jxta.impl.rendezvous.RendezVousServiceImpl)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Discovery service (net.jxta.impl.discovery.DiscoveryServiceImpl)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the HTTP Message Transport (net.jxta.impl.endpoint.servlethttp.ServletHttpTransport)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Peerinfo Service (net.jxta.impl.peer.PeerInfoServiceImpl)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Always Access Service (net.jxta.impl.access.always.AlwaysAccessService)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the TCP Message Transport (net.jxta.impl.endpoint.netty.NettyTransport)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Router Message Transport (net.jxta.impl.endpoint.router.EndpointRouter)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the IP Multicast Message Transport (net.jxta.impl.endpoint.mcast.McastTransport)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Resolver service (net.jxta.impl.resolver.ResolverServiceImpl)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : None Membership Service (net.jxta.impl.membership.none.NoneMembershipService)
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 627 net.jxta.impl.endpoint.EndpointServiceImpl.startApp()
Endpoint Service started.
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 313 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:54:45 午後 org.mortbay.http.HttpServer start
情報: Starting Jetty/4.2.27
10 01, 2017 5:54:45 午後 org.mortbay.http.HttpContext start
情報: Started HttpContext[/]
10 01, 2017 5:54:45 午後 org.mortbay.http.SocketListener start
情報: Started SocketListener on 0.0.0.0:9700
10 01, 2017 5:54:45 午後 org.mortbay.http.HttpServer start
情報: Started org.mortbay.http.HttpServer@cb0ed20
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 259 net.jxta.impl.endpoint.servlethttp.HttpMessageReceiver.start()
HTTP Servlet Transport started.
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 478 net.jxta.impl.endpoint.mcast.McastTransport.startApp()
IP Multicast Message Transport disabled.
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 327 net.jxta.impl.resolver.ResolverServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 141 net.jxta.impl.rendezvous.adhoc.AdhocPeerRdvService.<init>()
RendezVous Service is initialized for urn:jxta:jxta-WorldGroup as an ad hoc peer. 
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 365 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Rendezvous Service started
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:54:45 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1628 net.jxta.impl.discovery.DiscoveryServiceImpl.beEdge()
Already an Edge peer -- No Switch is needed.
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 547 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Discovery service started
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-WorldGroup[0,310656974] / [1537358694]] : Initialized routerSrdi
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-WorldGroup[0,310656974] / [1537358694]] : Starting SRDI GC Thread for routerSrdi
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 815 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
urn:jxta:jxta-WorldGroup[0,310656974] / [1537358694] : Router Message Transport started.
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 425 net.jxta.peergroup.NetPeerGroupFactory.newNetPeerGroup()
Instantiating net peer group : urn:jxta:jxta-NetGroup
	Parent : urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [1537358694]
	ID : urn:jxta:jxta-NetGroup
	Name : NetPeerGroup
	impl : <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE jxta:MIA>
<jxta:MIA xml:space="default" xmlns:jxta="http://jxta.org">
	<MSID>
		urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000010206
	</MSID>
	<Desc>
		Default Network PeerGroup reference implementation
	</Desc>
	<Comp>
		<Efmt>
			JRE1.5
		</Efmt>
		<Bind>
			V2.0 Ref Impl
		</Bind>
	</Comp>
	<Code>
		net.jxta.impl.peergroup.ShadowPeerGroup
	</Code>
	<PURI>
		http://download.java.net/jxta/jxta-jxse/latest/jnlp/lib/jxta.jar
	</PURI>
	<Prov>
		sun.com
	</Prov>
	<Parm>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000080106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000060106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000040106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000030106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000070106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000100106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000020106
		</Svc>
		<Svc>
			urn:jxta:uuid-DDC5CA55578E4AB99A0AA81D2DC6EF3F3F7E9F18B5D84DD58D21CE9E37E19E6C06
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000050106
		</Svc>
		<Proto>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000F0106
		</Proto>
		<Proto>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000B0106
		</Proto>
	</Parm>
</jxta:MIA>

10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 308 net.jxta.impl.cm.Srdi.clearSrdi()
Clearing SRDI for null
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 129 net.jxta.impl.cm.InMemorySrdi.clearSrdi()
Clearing SRDIs for urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [1537358694]
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Relay Message Transport (net.jxta.impl.endpoint.relay.RelayTransport)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Endpoint service (net.jxta.impl.endpoint.EndpointServiceImpl)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Rendezvous Service (net.jxta.impl.rendezvous.RendezVousServiceImpl)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Pipe Service (net.jxta.impl.pipe.PipeServiceImpl)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Discovery service (net.jxta.impl.discovery.DiscoveryServiceImpl)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Peerinfo Service (net.jxta.impl.peer.PeerInfoServiceImpl)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Always Access Service (net.jxta.impl.access.always.AlwaysAccessService)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Router Message Transport (net.jxta.impl.endpoint.router.EndpointRouter)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Resolver service (net.jxta.impl.resolver.ResolverServiceImpl)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Content Service (net.jxta.impl.content.ContentServiceImpl)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : None Membership Service (net.jxta.impl.membership.none.NoneMembershipService)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 233 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is an endpoint service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 627 net.jxta.impl.endpoint.EndpointServiceImpl.startApp()
Endpoint Service started.
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 313 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 293 net.jxta.impl.pipe.PipeServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 327 net.jxta.impl.resolver.ResolverServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 243 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is a discovery service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 509 net.jxta.impl.rendezvous.rpv.PeerView.<init>()
PeerView created for group "" [urn:jxta:jxta-NetGroup] name "urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000605jxta-NetGroup"
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 235 net.jxta.impl.rendezvous.rdv.RdvPeerRdvService.<init>()
RendezVous Service is initialized for urn:jxta:jxta-NetGroup as a Rendezvous peer.
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 110 net.jxta.impl.rendezvous.limited.LimitedRangeGreeter.<init>()
Listening on LR-Greeterurn:jxta:jxta-NetGroup/urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000605jxta-NetGroup
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 294 net.jxta.impl.rendezvous.rdv.RdvPeerRdvService.startApp()
RdvPeerRdvService is started
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 365 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Rendezvous Service started
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 293 net.jxta.impl.pipe.PipeServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 243 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is a discovery service
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [1537358694]] : Initialized pipeResolverSrdi
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [1537358694]] : Starting SRDI GC Thread for pipeResolverSrdi
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [1537358694]] : Initialized discoverySrdi
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [1537358694]] : Starting SRDI GC Thread for discoverySrdi
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1616 net.jxta.impl.discovery.DiscoveryServiceImpl.beRendezvous()
Switched to Rendezvous peer role.
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 547 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Discovery service started
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [1537358694]] : Initialized routerSrdi
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [1537358694]] : Starting SRDI GC Thread for routerSrdi
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 815 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [1537358694] : Router Message Transport started.
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 274 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Relay Message Transport started
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded module : Default Network PeerGroup reference implementation (net.jxta.impl.peergroup.ShadowPeerGroup)
10 01, 2017 5:54:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 424 net.jxta.platform.NetworkManager.startNetwork()
Started JXTA Network!
10 01, 2017 5:54:47 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 115 net.jxta.impl.pipe.InputPipeImpl.<init>()
Creating InputPipe for urn:jxta:uuid-59616261646162614E504720503250335065657256694577AA78B4612D4E657404 of type JxtaPropagate with listener
10 01, 2017 5:54:47 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 193 net.jxta.impl.pipe.WirePipe.register()
Registering urn:jxta:uuid-59616261646162614E504720503250335065657256694577AA78B4612D4E657404 with pipe resolver.
10 01, 2017 5:54:47 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 128 net.jxta.impl.pipe.NonBlockingWireOutputPipe.<init>()
Constructing for urn:jxta:uuid-59616261646162614E504720503250335065657256694577AA78B4612D4E657404
10 01, 2017 5:54:47 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1636 net.jxta.impl.rendezvous.rpv.PeerView.openWirePipes()
Propagate Pipes opened.
10 01, 2017 5:54:55 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 5:55:06 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 136 net.jxta.impl.endpoint.netty.NettyTransportClient.getMessenger()
processing request to open connection to tcp://10.1.0.4:9723
10 01, 2017 5:55:06 午後 net.jxta.impl.endpoint.netty.NettyTransportClient getMessenger
情報: succeeded in connecting to tcp://10.1.0.4:9723, remote peer has logical address jxta://uuid-59616261646162614E50472050325033456467652044496DA974F2692C20536503
10 01, 2017 5:55:17 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 115 net.jxta.impl.pipe.InputPipeImpl.<init>()
Creating InputPipe for urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6404 of type JxtaUnicast with listener
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE jxta:PipeAdvertisement>
<jxta:PipeAdvertisement xml:space="default" xmlns:jxta="http://jxta.org">
	<Id>
		urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6404
	</Id>
	<Type>
		JxtaUnicast
	</Type>
	<Name>
		Test Pipe
	</Name>
	<Desc>
		Created by RendezVous Chandra, Receiving Messages
	</Desc>
</jxta:PipeAdvertisement>

10 01, 2017 5:55:37 午後 net.jxta.impl.util.threads.LongTaskDetector run
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] still running after 1,002ms in thread JxtaWorker-4, current stack:
java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
java.awt.WaitDispatchSupport.enter(WaitDispatchSupport.java:257)
java.awt.Dialog.show(Dialog.java:1084)
javax.swing.JOptionPane.showOptionDialog(JOptionPane.java:869)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:666)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:637)
Examples.Z_Tools_And_Others.Tools.PopInformationMessage(Tools.java:141)
Examples.G_Simple_Pipe_Communication.RendezVous_Chandra_Receiving_Messages.pipeMsgEvent(RendezVous_Chandra_Receiving_Messages.java:81)
net.jxta.impl.pipe.InputPipeImpl.processIncomingMessage(InputPipeImpl.java:214)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.router.EndpointRouter.processIncomingMessage(EndpointRouter.java:1657)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.netty.NettyMessenger$1.run(NettyMessenger.java:148)
net.jxta.impl.util.threads.RunnableAsCallableWrapper.call(RunnableAsCallableWrapper.java:17)
net.jxta.impl.util.threads.RunMetricsWrapper.call(RunMetricsWrapper.java:50)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.call(QueueTimeRunMetricsWrapper.java:34)
net.jxta.impl.util.threads.RunMetricsWrapper.run(RunMetricsWrapper.java:93)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.run(QueueTimeRunMetricsWrapper.java:9)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
java.lang.Thread.run(Thread.java:745)

10 01, 2017 5:55:40 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 5:55:46 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 5:55:57 午後 net.jxta.impl.util.threads.LongTaskDetector run
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] still running after 21,002ms in thread JxtaWorker-4, current stack:
java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
java.awt.WaitDispatchSupport.enter(WaitDispatchSupport.java:257)
java.awt.Dialog.show(Dialog.java:1084)
javax.swing.JOptionPane.showOptionDialog(JOptionPane.java:869)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:666)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:637)
Examples.Z_Tools_And_Others.Tools.PopInformationMessage(Tools.java:141)
Examples.G_Simple_Pipe_Communication.RendezVous_Chandra_Receiving_Messages.pipeMsgEvent(RendezVous_Chandra_Receiving_Messages.java:81)
net.jxta.impl.pipe.InputPipeImpl.processIncomingMessage(InputPipeImpl.java:214)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.router.EndpointRouter.processIncomingMessage(EndpointRouter.java:1657)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.netty.NettyMessenger$1.run(NettyMessenger.java:148)
net.jxta.impl.util.threads.RunnableAsCallableWrapper.call(RunnableAsCallableWrapper.java:17)
net.jxta.impl.util.threads.RunMetricsWrapper.call(RunMetricsWrapper.java:50)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.call(QueueTimeRunMetricsWrapper.java:34)
net.jxta.impl.util.threads.RunMetricsWrapper.run(RunMetricsWrapper.java:93)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.run(QueueTimeRunMetricsWrapper.java:9)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
java.lang.Thread.run(Thread.java:745)

10 01, 2017 5:56:17 午後 net.jxta.impl.util.threads.LongTaskDetector run
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] still running after 41,001ms in thread JxtaWorker-4, current stack:
java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
java.awt.WaitDispatchSupport.enter(WaitDispatchSupport.java:257)
java.awt.Dialog.show(Dialog.java:1084)
javax.swing.JOptionPane.showOptionDialog(JOptionPane.java:869)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:666)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:637)
Examples.Z_Tools_And_Others.Tools.PopInformationMessage(Tools.java:141)
Examples.G_Simple_Pipe_Communication.RendezVous_Chandra_Receiving_Messages.pipeMsgEvent(RendezVous_Chandra_Receiving_Messages.java:81)
net.jxta.impl.pipe.InputPipeImpl.processIncomingMessage(InputPipeImpl.java:214)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.router.EndpointRouter.processIncomingMessage(EndpointRouter.java:1657)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.netty.NettyMessenger$1.run(NettyMessenger.java:148)
net.jxta.impl.util.threads.RunnableAsCallableWrapper.call(RunnableAsCallableWrapper.java:17)
net.jxta.impl.util.threads.RunMetricsWrapper.call(RunMetricsWrapper.java:50)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.call(QueueTimeRunMetricsWrapper.java:34)
net.jxta.impl.util.threads.RunMetricsWrapper.run(RunMetricsWrapper.java:93)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.run(QueueTimeRunMetricsWrapper.java:9)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
java.lang.Thread.run(Thread.java:745)

10 01, 2017 5:56:37 午後 net.jxta.impl.util.threads.LongTaskDetector run
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] still running after 61,001ms in thread JxtaWorker-4, current stack:
java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
java.awt.WaitDispatchSupport.enter(WaitDispatchSupport.java:257)
java.awt.Dialog.show(Dialog.java:1084)
javax.swing.JOptionPane.showOptionDialog(JOptionPane.java:869)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:666)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:637)
Examples.Z_Tools_And_Others.Tools.PopInformationMessage(Tools.java:141)
Examples.G_Simple_Pipe_Communication.RendezVous_Chandra_Receiving_Messages.pipeMsgEvent(RendezVous_Chandra_Receiving_Messages.java:81)
net.jxta.impl.pipe.InputPipeImpl.processIncomingMessage(InputPipeImpl.java:214)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.router.EndpointRouter.processIncomingMessage(EndpointRouter.java:1657)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.netty.NettyMessenger$1.run(NettyMessenger.java:148)
net.jxta.impl.util.threads.RunnableAsCallableWrapper.call(RunnableAsCallableWrapper.java:17)
net.jxta.impl.util.threads.RunMetricsWrapper.call(RunMetricsWrapper.java:50)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.call(QueueTimeRunMetricsWrapper.java:34)
net.jxta.impl.util.threads.RunMetricsWrapper.run(RunMetricsWrapper.java:93)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.run(QueueTimeRunMetricsWrapper.java:9)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
java.lang.Thread.run(Thread.java:745)

10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 483 net.jxta.platform.NetworkManager.stopNetwork()
Stopping JXTA Network!
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 302 net.jxta.impl.endpoint.relay.RelayTransport.stopApp()
Relay Message Transport stopped
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 845 net.jxta.impl.endpoint.router.EndpointRouter.stopApp()
urn:jxta:jxta-NetGroup "NetPeerGroup"[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [1537358694] : Router Message Transport stopped.
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 590 net.jxta.impl.discovery.DiscoveryServiceImpl.stopApp()
Discovery service stopped.
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 215 net.jxta.impl.pipe.WirePipe.forget()
Deregistering wire pipe with pipe resolver
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 181 net.jxta.impl.pipe.InputPipeImpl.close()
close() : pipe was not registered with registrar.
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 186 net.jxta.impl.pipe.InputPipeImpl.close()
Closed urn:jxta:uuid-59616261646162614E504720503250335065657256694577AA78B4612D4E657404
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 186 net.jxta.impl.pipe.InputPipeImpl.close()
Closed urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04368616E6404
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 139 net.jxta.impl.pipe.NonBlockingWireOutputPipe.close()
Closing queue for urn:jxta:uuid-59616261646162614E504720503250335065657256694577AA78B4612D4E657404
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1662 net.jxta.impl.rendezvous.rpv.PeerView.closeWirePipes()
Propagate Pipes closed.
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 389 net.jxta.impl.rendezvous.RendezVousServiceImpl.stopApp()
Rendezvous Serivce stopped
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 686 net.jxta.impl.endpoint.EndpointServiceImpl.stopApp()
Endpoint Service stopped.
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 202 net.jxta.impl.cm.XIndiceIndexer.close()
Closing Indexer
10 01, 2017 5:56:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 501 net.jxta.platform.NetworkManager.stopNetwork()
Stopped JXTA Network!
10 01, 2017 5:56:47 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 1059 net.jxta.impl.endpoint.router.EndpointRouter.getGatewayAddress()
getGatewayAddress exception
java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask@78208787 rejected from net.jxta.impl.util.threads.SharedScheduledThreadPoolExecutor@3d2216e7[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 6]
Line 2047 java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution()
Line 823 java.util.concurrent.ThreadPoolExecutor.reject()
Line 326 java.util.concurrent.ScheduledThreadPoolExecutor.delayedExecute()
Line 573 java.util.concurrent.ScheduledThreadPoolExecutor.scheduleAtFixedRate()
Line 59 net.jxta.impl.util.threads.SharedScheduledThreadPoolExecutor.scheduleAtFixedRate()
Line 285 net.jxta.impl.endpoint.router.EndpointRouter$ClearPendingQuery.<init>()
Line 1011 net.jxta.impl.endpoint.router.EndpointRouter.getGatewayAddress()
Line 2412 net.jxta.impl.endpoint.router.EndpointRouter.addressMessage()
Line 169 net.jxta.impl.endpoint.router.RouterMessenger.sendMessageBImpl()
Line 789 net.jxta.impl.endpoint.BlockingMessenger.sendIt()
Line 742 net.jxta.impl.endpoint.BlockingMessenger.performDeferredAction()
Line 608 net.jxta.impl.endpoint.BlockingMessenger.sendMessageB()
Line 467 net.jxta.impl.endpoint.EndpointServiceImpl$CanonicalMessenger.sendMessageBImpl()
Line 490 net.jxta.endpoint.ThreadedMessenger.send()
Line 381 net.jxta.endpoint.ThreadedMessenger.run()
Line 745 java.lang.Thread.run()

10 01, 2017 5:56:48 午後 net.jxta.impl.util.threads.RunMetricsWrapper call
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] took 71,428ms to complete in the shared executor

```

## 双方向通信

### Adelaide

``` log

run:
10 01, 2017 6:13:58 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 355 net.jxta.platform.NetworkManager.configure()
Created new configuration. mode = RENDEZVOUS
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 404 net.jxta.platform.NetworkManager.startNetwork()
Starting JXTA Network! MODE = RENDEZVOUS,  HOME = file:/C:/Users/rocko/Documents/Laboratory/NetBeans/Adelaide/./RendezVous%20Adelaide,%20at%20one%20end
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 286 net.jxta.peergroup.WorldPeerGroupFactory.newWorldPeerGroup()
Making a new World Peer Group instance using : net.jxta.impl.peergroup.Platform
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 308 net.jxta.impl.cm.Srdi.clearSrdi()
Clearing SRDI for null
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 129 net.jxta.impl.cm.InMemorySrdi.clearSrdi()
Clearing SRDIs for urn:jxta:jxta-WorldGroup[0,310656974] / [2109957412]
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Endpoint service (net.jxta.impl.endpoint.EndpointServiceImpl)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Rendezvous Service (net.jxta.impl.rendezvous.RendezVousServiceImpl)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Discovery service (net.jxta.impl.discovery.DiscoveryServiceImpl)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the HTTP Message Transport (net.jxta.impl.endpoint.servlethttp.ServletHttpTransport)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Peerinfo Service (net.jxta.impl.peer.PeerInfoServiceImpl)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Always Access Service (net.jxta.impl.access.always.AlwaysAccessService)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the TCP Message Transport (net.jxta.impl.endpoint.netty.NettyTransport)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Router Message Transport (net.jxta.impl.endpoint.router.EndpointRouter)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the IP Multicast Message Transport (net.jxta.impl.endpoint.mcast.McastTransport)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Resolver service (net.jxta.impl.resolver.ResolverServiceImpl)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : None Membership Service (net.jxta.impl.membership.none.NoneMembershipService)
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 627 net.jxta.impl.endpoint.EndpointServiceImpl.startApp()
Endpoint Service started.
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 313 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 6:14:07 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:08 午後 org.mortbay.http.HttpServer start
情報: Starting Jetty/4.2.27
10 01, 2017 6:14:08 午後 org.mortbay.http.HttpContext start
情報: Started HttpContext[/]
10 01, 2017 6:14:08 午後 org.mortbay.http.SocketListener start
情報: Started SocketListener on 0.0.0.0:9700
10 01, 2017 6:14:08 午後 org.mortbay.http.HttpServer start
情報: Started org.mortbay.http.HttpServer@cb0ed20
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 259 net.jxta.impl.endpoint.servlethttp.HttpMessageReceiver.start()
HTTP Servlet Transport started.
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 478 net.jxta.impl.endpoint.mcast.McastTransport.startApp()
IP Multicast Message Transport disabled.
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 327 net.jxta.impl.resolver.ResolverServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 141 net.jxta.impl.rendezvous.adhoc.AdhocPeerRdvService.<init>()
RendezVous Service is initialized for urn:jxta:jxta-WorldGroup as an ad hoc peer. 
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 365 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Rendezvous Service started
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1628 net.jxta.impl.discovery.DiscoveryServiceImpl.beEdge()
Already an Edge peer -- No Switch is needed.
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 547 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Discovery service started
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-WorldGroup[0,310656974] / [2109957412]] : Initialized routerSrdi
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-WorldGroup[0,310656974] / [2109957412]] : Starting SRDI GC Thread for routerSrdi
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 815 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
urn:jxta:jxta-WorldGroup[0,310656974] / [2109957412] : Router Message Transport started.
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 425 net.jxta.peergroup.NetPeerGroupFactory.newNetPeerGroup()
Instantiating net peer group : urn:jxta:jxta-NetGroup
	Parent : urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [2109957412]
	ID : urn:jxta:jxta-NetGroup
	Name : NetPeerGroup
	impl : <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE jxta:MIA>
<jxta:MIA xml:space="default" xmlns:jxta="http://jxta.org">
	<MSID>
		urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000010206
	</MSID>
	<Desc>
		Default Network PeerGroup reference implementation
	</Desc>
	<Comp>
		<Efmt>
			JRE1.5
		</Efmt>
		<Bind>
			V2.0 Ref Impl
		</Bind>
	</Comp>
	<Code>
		net.jxta.impl.peergroup.ShadowPeerGroup
	</Code>
	<PURI>
		http://download.java.net/jxta/jxta-jxse/latest/jnlp/lib/jxta.jar
	</PURI>
	<Prov>
		sun.com
	</Prov>
	<Parm>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000080106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000060106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000040106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000030106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000070106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000100106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000020106
		</Svc>
		<Svc>
			urn:jxta:uuid-DDC5CA55578E4AB99A0AA81D2DC6EF3F3F7E9F18B5D84DD58D21CE9E37E19E6C06
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000050106
		</Svc>
		<Proto>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000F0106
		</Proto>
		<Proto>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000B0106
		</Proto>
	</Parm>
</jxta:MIA>

10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 308 net.jxta.impl.cm.Srdi.clearSrdi()
Clearing SRDI for null
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 129 net.jxta.impl.cm.InMemorySrdi.clearSrdi()
Clearing SRDIs for urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [2109957412]
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Relay Message Transport (net.jxta.impl.endpoint.relay.RelayTransport)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Endpoint service (net.jxta.impl.endpoint.EndpointServiceImpl)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Rendezvous Service (net.jxta.impl.rendezvous.RendezVousServiceImpl)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Pipe Service (net.jxta.impl.pipe.PipeServiceImpl)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Discovery service (net.jxta.impl.discovery.DiscoveryServiceImpl)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Peerinfo Service (net.jxta.impl.peer.PeerInfoServiceImpl)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Always Access Service (net.jxta.impl.access.always.AlwaysAccessService)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Router Message Transport (net.jxta.impl.endpoint.router.EndpointRouter)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Resolver service (net.jxta.impl.resolver.ResolverServiceImpl)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Content Service (net.jxta.impl.content.ContentServiceImpl)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : None Membership Service (net.jxta.impl.membership.none.NoneMembershipService)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 233 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is an endpoint service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 627 net.jxta.impl.endpoint.EndpointServiceImpl.startApp()
Endpoint Service started.
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 313 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 293 net.jxta.impl.pipe.PipeServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 327 net.jxta.impl.resolver.ResolverServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 243 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is a discovery service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 509 net.jxta.impl.rendezvous.rpv.PeerView.<init>()
PeerView created for group "" [urn:jxta:jxta-NetGroup] name "urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000605jxta-NetGroup"
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 235 net.jxta.impl.rendezvous.rdv.RdvPeerRdvService.<init>()
RendezVous Service is initialized for urn:jxta:jxta-NetGroup as a Rendezvous peer.
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 110 net.jxta.impl.rendezvous.limited.LimitedRangeGreeter.<init>()
Listening on LR-Greeterurn:jxta:jxta-NetGroup/urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000605jxta-NetGroup
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 294 net.jxta.impl.rendezvous.rdv.RdvPeerRdvService.startApp()
RdvPeerRdvService is started
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 365 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Rendezvous Service started
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 293 net.jxta.impl.pipe.PipeServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 243 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is a discovery service
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [2109957412]] : Initialized pipeResolverSrdi
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [2109957412]] : Starting SRDI GC Thread for pipeResolverSrdi
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [2109957412]] : Initialized discoverySrdi
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [2109957412]] : Starting SRDI GC Thread for discoverySrdi
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1616 net.jxta.impl.discovery.DiscoveryServiceImpl.beRendezvous()
Switched to Rendezvous peer role.
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 547 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Discovery service started
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [2109957412]] : Initialized routerSrdi
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [2109957412]] : Starting SRDI GC Thread for routerSrdi
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 815 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
urn:jxta:jxta-NetGroup[0,1237598030] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,310656974] / [2109957412] : Router Message Transport started.
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 274 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Relay Message Transport started
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded module : Default Network PeerGroup reference implementation (net.jxta.impl.peergroup.ShadowPeerGroup)
10 01, 2017 6:14:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 424 net.jxta.platform.NetworkManager.startNetwork()
Started JXTA Network!
10 01, 2017 6:14:09 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 115 net.jxta.impl.pipe.InputPipeImpl.<init>()
Creating InputPipe for urn:jxta:uuid-59616261646162614E504720503250335065657256694577AA78B4612D4E657404 of type JxtaPropagate with listener
10 01, 2017 6:14:09 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 193 net.jxta.impl.pipe.WirePipe.register()
Registering urn:jxta:uuid-59616261646162614E504720503250335065657256694577AA78B4612D4E657404 with pipe resolver.
10 01, 2017 6:14:09 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 128 net.jxta.impl.pipe.NonBlockingWireOutputPipe.<init>()
Constructing for urn:jxta:uuid-59616261646162614E504720503250335065657256694577AA78B4612D4E657404
10 01, 2017 6:14:09 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1636 net.jxta.impl.rendezvous.rpv.PeerView.openWirePipes()
Propagate Pipes opened.
10 01, 2017 6:14:17 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 136 net.jxta.impl.endpoint.netty.NettyTransportClient.getMessenger()
processing request to open connection to tcp://10.1.0.4:9725
10 01, 2017 6:14:30 午後 net.jxta.impl.endpoint.netty.NettyTransportClient getMessenger
情報: succeeded in connecting to tcp://10.1.0.4:9725, remote peer has logical address jxta://uuid-59616261646162614E504720503250334564676520514569AE69F3656C612C2003
10 01, 2017 6:14:45 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 115 net.jxta.impl.pipe.InputPipeImpl.<init>()
Creating InputPipe for urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04164656C6104 of type JxtaUnicast with listener
10 01, 2017 6:14:49 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 115 net.jxta.impl.pipe.InputPipeImpl.<init>()
Creating InputPipe for urn:jxta:uuid-59616261646162614E50472050325033F2EA5A2A3D7D47C497A6F626BDDB2A3304 of type JxtaUnicast with listener
10 01, 2017 6:15:01 午後 net.jxta.impl.util.threads.LongTaskDetector run
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] still running after 1,002ms in thread JxtaWorker-2, current stack:
java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
java.awt.WaitDispatchSupport.enter(WaitDispatchSupport.java:257)
java.awt.Dialog.show(Dialog.java:1084)
javax.swing.JOptionPane.showOptionDialog(JOptionPane.java:869)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:666)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:637)
Examples.Z_Tools_And_Others.Tools.PopInformationMessage(Tools.java:141)
Examples.H_Bidirectional_Pipe_Communication.RendezVous_Adelaide_At_One_End.pipeMsgEvent(RendezVous_Adelaide_At_One_End.java:79)
net.jxta.util.JxtaBiDiPipe.push(JxtaBiDiPipe.java:1041)
net.jxta.util.JxtaBiDiPipe.pipeMsgEvent(JxtaBiDiPipe.java:882)
net.jxta.impl.pipe.InputPipeImpl.processIncomingMessage(InputPipeImpl.java:214)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.router.EndpointRouter.processIncomingMessage(EndpointRouter.java:1657)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.netty.NettyMessenger$1.run(NettyMessenger.java:148)
net.jxta.impl.util.threads.RunnableAsCallableWrapper.call(RunnableAsCallableWrapper.java:17)
net.jxta.impl.util.threads.RunMetricsWrapper.call(RunMetricsWrapper.java:50)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.call(QueueTimeRunMetricsWrapper.java:34)
net.jxta.impl.util.threads.RunMetricsWrapper.run(RunMetricsWrapper.java:93)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.run(QueueTimeRunMetricsWrapper.java:9)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
java.lang.Thread.run(Thread.java:745)

10 01, 2017 6:15:02 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:15:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:15:11 午後 net.jxta.impl.util.threads.LongTaskDetector run
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] still running after 1,003ms in thread JxtaWorker-5, current stack:
java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
java.awt.WaitDispatchSupport.enter(WaitDispatchSupport.java:257)
java.awt.Dialog.show(Dialog.java:1084)
javax.swing.JOptionPane.showOptionDialog(JOptionPane.java:869)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:666)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:637)
Examples.Z_Tools_And_Others.Tools.PopInformationMessage(Tools.java:141)
Examples.H_Bidirectional_Pipe_Communication.RendezVous_Adelaide_At_One_End.pipeMsgEvent(RendezVous_Adelaide_At_One_End.java:79)
net.jxta.util.JxtaBiDiPipe.push(JxtaBiDiPipe.java:1041)
net.jxta.util.JxtaBiDiPipe.pipeMsgEvent(JxtaBiDiPipe.java:882)
net.jxta.impl.pipe.InputPipeImpl.processIncomingMessage(InputPipeImpl.java:214)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.router.EndpointRouter.processIncomingMessage(EndpointRouter.java:1657)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.netty.NettyMessenger$1.run(NettyMessenger.java:148)
net.jxta.impl.util.threads.RunnableAsCallableWrapper.call(RunnableAsCallableWrapper.java:17)
net.jxta.impl.util.threads.RunMetricsWrapper.call(RunMetricsWrapper.java:50)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.call(QueueTimeRunMetricsWrapper.java:34)
net.jxta.impl.util.threads.RunMetricsWrapper.run(RunMetricsWrapper.java:93)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.run(QueueTimeRunMetricsWrapper.java:9)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
java.lang.Thread.run(Thread.java:745)

10 01, 2017 6:15:20 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 186 net.jxta.impl.pipe.InputPipeImpl.close()
Closed urn:jxta:uuid-59616261646162614E50472050325033F2EA5A2A3D7D47C497A6F626BDDB2A3304
10 01, 2017 6:15:21 午後 net.jxta.impl.util.threads.LongTaskDetector run
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] still running after 21,001ms in thread JxtaWorker-2, current stack:
java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
java.awt.WaitDispatchSupport.enter(WaitDispatchSupport.java:257)
java.awt.Dialog.show(Dialog.java:1084)
javax.swing.JOptionPane.showOptionDialog(JOptionPane.java:869)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:666)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:637)
Examples.Z_Tools_And_Others.Tools.PopInformationMessage(Tools.java:141)
Examples.H_Bidirectional_Pipe_Communication.RendezVous_Adelaide_At_One_End.pipeMsgEvent(RendezVous_Adelaide_At_One_End.java:79)
net.jxta.util.JxtaBiDiPipe.push(JxtaBiDiPipe.java:1041)
net.jxta.util.JxtaBiDiPipe.pipeMsgEvent(JxtaBiDiPipe.java:882)
net.jxta.impl.pipe.InputPipeImpl.processIncomingMessage(InputPipeImpl.java:214)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.router.EndpointRouter.processIncomingMessage(EndpointRouter.java:1657)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.netty.NettyMessenger$1.run(NettyMessenger.java:148)
net.jxta.impl.util.threads.RunnableAsCallableWrapper.call(RunnableAsCallableWrapper.java:17)
net.jxta.impl.util.threads.RunMetricsWrapper.call(RunMetricsWrapper.java:50)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.call(QueueTimeRunMetricsWrapper.java:34)
net.jxta.impl.util.threads.RunMetricsWrapper.run(RunMetricsWrapper.java:93)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.run(QueueTimeRunMetricsWrapper.java:9)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
java.lang.Thread.run(Thread.java:745)

10 01, 2017 6:15:31 午後 net.jxta.impl.util.threads.LongTaskDetector run
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] still running after 21,002ms in thread JxtaWorker-5, current stack:
java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
java.awt.WaitDispatchSupport.enter(WaitDispatchSupport.java:257)
java.awt.Dialog.show(Dialog.java:1084)
javax.swing.JOptionPane.showOptionDialog(JOptionPane.java:869)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:666)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:637)
Examples.Z_Tools_And_Others.Tools.PopInformationMessage(Tools.java:141)
Examples.H_Bidirectional_Pipe_Communication.RendezVous_Adelaide_At_One_End.pipeMsgEvent(RendezVous_Adelaide_At_One_End.java:79)
net.jxta.util.JxtaBiDiPipe.push(JxtaBiDiPipe.java:1041)
net.jxta.util.JxtaBiDiPipe.pipeMsgEvent(JxtaBiDiPipe.java:882)
net.jxta.impl.pipe.InputPipeImpl.processIncomingMessage(InputPipeImpl.java:214)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.router.EndpointRouter.processIncomingMessage(EndpointRouter.java:1657)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.netty.NettyMessenger$1.run(NettyMessenger.java:148)
net.jxta.impl.util.threads.RunnableAsCallableWrapper.call(RunnableAsCallableWrapper.java:17)
net.jxta.impl.util.threads.RunMetricsWrapper.call(RunMetricsWrapper.java:50)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.call(QueueTimeRunMetricsWrapper.java:34)
net.jxta.impl.util.threads.RunMetricsWrapper.run(RunMetricsWrapper.java:93)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.run(QueueTimeRunMetricsWrapper.java:9)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
java.lang.Thread.run(Thread.java:745)

10 01, 2017 6:15:41 午後 net.jxta.impl.util.threads.LongTaskDetector run
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] still running after 41,000ms in thread JxtaWorker-2, current stack:
java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
java.awt.WaitDispatchSupport.enter(WaitDispatchSupport.java:257)
java.awt.Dialog.show(Dialog.java:1084)
javax.swing.JOptionPane.showOptionDialog(JOptionPane.java:869)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:666)
javax.swing.JOptionPane.showMessageDialog(JOptionPane.java:637)
Examples.Z_Tools_And_Others.Tools.PopInformationMessage(Tools.java:141)
Examples.H_Bidirectional_Pipe_Communication.RendezVous_Adelaide_At_One_End.pipeMsgEvent(RendezVous_Adelaide_At_One_End.java:79)
net.jxta.util.JxtaBiDiPipe.push(JxtaBiDiPipe.java:1041)
net.jxta.util.JxtaBiDiPipe.pipeMsgEvent(JxtaBiDiPipe.java:882)
net.jxta.impl.pipe.InputPipeImpl.processIncomingMessage(InputPipeImpl.java:214)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.router.EndpointRouter.processIncomingMessage(EndpointRouter.java:1657)
net.jxta.impl.endpoint.EndpointServiceImpl.processIncomingMessage(EndpointServiceImpl.java:1027)
net.jxta.impl.endpoint.netty.NettyMessenger$1.run(NettyMessenger.java:148)
net.jxta.impl.util.threads.RunnableAsCallableWrapper.call(RunnableAsCallableWrapper.java:17)
net.jxta.impl.util.threads.RunMetricsWrapper.call(RunMetricsWrapper.java:50)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.call(QueueTimeRunMetricsWrapper.java:34)
net.jxta.impl.util.threads.RunMetricsWrapper.run(RunMetricsWrapper.java:93)
net.jxta.impl.util.threads.QueueTimeRunMetricsWrapper.run(QueueTimeRunMetricsWrapper.java:9)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
java.lang.Thread.run(Thread.java:745)

10 01, 2017 6:15:51 午後 net.jxta.impl.util.threads.RunMetricsWrapper call
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] took 40,072ms to complete in the shared executor
10 01, 2017 6:15:54 午後 net.jxta.impl.util.threads.RunMetricsWrapper call
警告: task of type [net.jxta.impl.endpoint.netty.NettyMessenger$1] took 53,776ms to complete in the shared executor
10 01, 2017 6:16:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:16:35 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:17:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:18:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:18:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:19:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:19:41 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:20:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:21:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:21:14 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:22:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:22:47 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:23:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:24:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:24:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:24:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:24:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:24:20 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:25:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:25:53 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:26:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:27:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:27:26 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:28:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:28:59 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:29:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:30:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:30:32 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:31:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:32:05 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:32:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:33:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:33:38 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:34:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:34:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:34:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:34:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:35:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:35:11 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:36:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:36:44 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:37:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:38:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:38:17 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...
10 01, 2017 6:39:08 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:39:50 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 996 net.jxta.impl.rendezvous.rpv.PeerView.seed()
New Seeding...


```

### Quinisela

``` log
run:
10 01, 2017 6:14:26 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 355 net.jxta.platform.NetworkManager.configure()
Created new configuration. mode = EDGE
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 404 net.jxta.platform.NetworkManager.startNetwork()
Starting JXTA Network! MODE = EDGE,  HOME = file:/C:/Users/rocko/Documents/Laboratory/NetBeans/Quinisela/./Edge%20Quinisela,%20at%20the%20other%20end
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 286 net.jxta.peergroup.WorldPeerGroupFactory.newWorldPeerGroup()
Making a new World Peer Group instance using : net.jxta.impl.peergroup.Platform
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 308 net.jxta.impl.cm.Srdi.clearSrdi()
Clearing SRDI for null
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 129 net.jxta.impl.cm.InMemorySrdi.clearSrdi()
Clearing SRDIs for urn:jxta:jxta-WorldGroup[0,194494468] / [2088051243]
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Endpoint service (net.jxta.impl.endpoint.EndpointServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Rendezvous Service (net.jxta.impl.rendezvous.RendezVousServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Discovery service (net.jxta.impl.discovery.DiscoveryServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the HTTP Message Transport (net.jxta.impl.endpoint.servlethttp.ServletHttpTransport)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Peerinfo Service (net.jxta.impl.peer.PeerInfoServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Always Access Service (net.jxta.impl.access.always.AlwaysAccessService)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the TCP Message Transport (net.jxta.impl.endpoint.netty.NettyTransport)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Router Message Transport (net.jxta.impl.endpoint.router.EndpointRouter)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the IP Multicast Message Transport (net.jxta.impl.endpoint.mcast.McastTransport)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Resolver service (net.jxta.impl.resolver.ResolverServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : None Membership Service (net.jxta.impl.membership.none.NoneMembershipService)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 627 net.jxta.impl.endpoint.EndpointServiceImpl.startApp()
Endpoint Service started.
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 313 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 145 net.jxta.impl.endpoint.servlethttp.HttpMessageSender.start()
HTTP Client Transport started.
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 535 net.jxta.impl.endpoint.mcast.McastTransport.startApp()
IP Multicast Message Transport started.
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 327 net.jxta.impl.resolver.ResolverServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 141 net.jxta.impl.rendezvous.adhoc.AdhocPeerRdvService.<init>()
RendezVous Service is initialized for urn:jxta:jxta-WorldGroup as an ad hoc peer. 
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 365 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Rendezvous Service started
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1628 net.jxta.impl.discovery.DiscoveryServiceImpl.beEdge()
Already an Edge peer -- No Switch is needed.
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 547 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Discovery service started
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-WorldGroup[0,194494468] / [2088051243]] : Initialized routerSrdi
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-WorldGroup[0,194494468] / [2088051243]] : Starting SRDI GC Thread for routerSrdi
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 815 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
urn:jxta:jxta-WorldGroup[0,194494468] / [2088051243] : Router Message Transport started.
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 425 net.jxta.peergroup.NetPeerGroupFactory.newNetPeerGroup()
Instantiating net peer group : urn:jxta:jxta-NetGroup
	Parent : urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]
	ID : urn:jxta:jxta-NetGroup
	Name : NetPeerGroup
	impl : <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE jxta:MIA>
<jxta:MIA xml:space="default" xmlns:jxta="http://jxta.org">
	<MSID>
		urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000010206
	</MSID>
	<Desc>
		Default Network PeerGroup reference implementation
	</Desc>
	<Comp>
		<Efmt>
			JRE1.5
		</Efmt>
		<Bind>
			V2.0 Ref Impl
		</Bind>
	</Comp>
	<Code>
		net.jxta.impl.peergroup.ShadowPeerGroup
	</Code>
	<PURI>
		http://download.java.net/jxta/jxta-jxse/latest/jnlp/lib/jxta.jar
	</PURI>
	<Prov>
		sun.com
	</Prov>
	<Parm>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000080106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000060106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000040106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000030106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000070106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000100106
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000020106
		</Svc>
		<Svc>
			urn:jxta:uuid-DDC5CA55578E4AB99A0AA81D2DC6EF3F3F7E9F18B5D84DD58D21CE9E37E19E6C06
		</Svc>
		<Svc>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE000000050106
		</Svc>
		<Proto>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000F0106
		</Proto>
		<Proto>
			urn:jxta:uuid-DEADBEEFDEAFBABAFEEDBABE0000000B0106
		</Proto>
	</Parm>
</jxta:MIA>

10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 308 net.jxta.impl.cm.Srdi.clearSrdi()
Clearing SRDI for null
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 129 net.jxta.impl.cm.InMemorySrdi.clearSrdi()
Clearing SRDIs for urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Relay Message Transport (net.jxta.impl.endpoint.relay.RelayTransport)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Endpoint service (net.jxta.impl.endpoint.EndpointServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Rendezvous Service (net.jxta.impl.rendezvous.RendezVousServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Pipe Service (net.jxta.impl.pipe.PipeServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Discovery service (net.jxta.impl.discovery.DiscoveryServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Peerinfo Service (net.jxta.impl.peer.PeerInfoServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Always Access Service (net.jxta.impl.access.always.AlwaysAccessService)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Router Message Transport (net.jxta.impl.endpoint.router.EndpointRouter)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Resolver service (net.jxta.impl.resolver.ResolverServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : Reference Implementation of the Content Service (net.jxta.impl.content.ContentServiceImpl)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded privileged module : None Membership Service (net.jxta.impl.membership.none.NoneMembershipService)
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 233 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is an endpoint service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 627 net.jxta.impl.endpoint.EndpointServiceImpl.startApp()
Endpoint Service started.
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 313 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 293 net.jxta.impl.pipe.PipeServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 327 net.jxta.impl.resolver.ResolverServiceImpl.startApp()
Stalled until there is a membership service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 243 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is a discovery service
EdgePeerRdvService: urn:jxta:jxta-NetGroup
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 220 net.jxta.impl.rendezvous.edge.EdgePeerRdvService.<init>()
RendezVous Service is initialized for urn:jxta:jxta-NetGroup as an Edge peer.
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 365 net.jxta.impl.rendezvous.RendezVousServiceImpl.startApp()
Rendezvous Service started
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 293 net.jxta.impl.pipe.PipeServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 493 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 193 net.jxta.impl.peer.PeerInfoServiceImpl.startApp()
Stalled until there is a resolver service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 719 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
Endpoint Router start stalled until resolver service available
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 243 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Stalled until there is a discovery service
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]] : Initialized pipeResolverSrdi
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]] : Starting SRDI GC Thread for pipeResolverSrdi
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 1628 net.jxta.impl.discovery.DiscoveryServiceImpl.beEdge()
Already an Edge peer -- No Switch is needed.
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 547 net.jxta.impl.discovery.DiscoveryServiceImpl.startApp()
Discovery service started
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 123 net.jxta.impl.cm.InMemorySrdi.<init>()
[urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]] : Initialized routerSrdi
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 121 net.jxta.impl.cm.Srdi.<init>()
[urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243]] : Starting SRDI GC Thread for routerSrdi
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 136 net.jxta.impl.endpoint.netty.NettyTransportClient.getMessenger()
processing request to open connection to tcp://192.168.56.1:9726
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 815 net.jxta.impl.endpoint.router.EndpointRouter.startApp()
urn:jxta:jxta-NetGroup[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243] : Router Message Transport started.
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 215 net.jxta.impl.endpoint.relay.RelayClient.startClient()
Started client : relay://uuid-59616261646162614E504720503250334564676520514569AE69F3656C612C2003
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 274 net.jxta.impl.endpoint.relay.RelayTransport.startApp()
Relay Message Transport started
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 314 net.jxta.impl.endpoint.relay.RelayClient.run()
Start relay client thread
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 739 net.jxta.impl.peergroup.GenericPeerGroup.loadModule()
Loaded module : Default Network PeerGroup reference implementation (net.jxta.impl.peergroup.ShadowPeerGroup)
10 01, 2017 6:14:30 午後 net.jxta.impl.endpoint.netty.NettyTransportClient getMessenger
情報: succeeded in connecting to tcp://192.168.56.1:9726, remote peer has logical address jxta://uuid-59616261646162614E5047205032503352656E64657A466FB573A04164656C6103
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 424 net.jxta.platform.NetworkManager.startNetwork()
Started JXTA Network!
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 136 net.jxta.impl.endpoint.netty.NettyTransportClient.getMessenger()
processing request to open connection to tcp://10.1.0.4:9726
10 01, 2017 6:14:30 午後 net.jxta.impl.endpoint.netty.NettyTransportClient getMessenger
情報: succeeded in connecting to tcp://10.1.0.4:9726, remote peer has logical address jxta://uuid-59616261646162614E5047205032503352656E64657A466FB573A04164656C6103
10 01, 2017 6:14:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 555 net.jxta.impl.rendezvous.edge.EdgePeerRdvService.addRdv()
New RDV lease from urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04164656C6103 C : -1506849270966 / -1506849270965
10 01, 2017 6:14:49 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 115 net.jxta.impl.pipe.InputPipeImpl.<init>()
Creating InputPipe for urn:jxta:uuid-59616261646162614E5047205032503346CAECB2F41346EE9FAD9703465A7B1904 of type JxtaUnicast with listener
10 01, 2017 6:14:49 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 241 net.jxta.impl.pipe.NonBlockingOutputPipe.<init>()
Constructing for urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04164656C6104
10 01, 2017 6:14:49 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 675 net.jxta.impl.pipe.NonBlockingOutputPipe.startServiceThread()
Thread start : Worker Thread for NonBlockingOutputPipe : urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04164656C6104
	worker state : ACQUIREMESSENGER
10 01, 2017 6:14:59 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 657 net.jxta.impl.pipe.NonBlockingOutputPipe.run()
Thread exit : Worker Thread for NonBlockingOutputPipe : urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04164656C6104
	worker state : SENDMESSAGES
10 01, 2017 6:15:20 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 186 net.jxta.impl.pipe.InputPipeImpl.close()
Closed urn:jxta:uuid-59616261646162614E5047205032503346CAECB2F41346EE9FAD9703465A7B1904
10 01, 2017 6:15:30 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 183 net.jxta.impl.cm.InMemorySrdi.garbageCollect()
gc... 
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 483 net.jxta.platform.NetworkManager.stopNetwork()
Stopping JXTA Network!
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 264 net.jxta.impl.endpoint.relay.RelayClient.stopClient()
Stopped client : relay://uuid-59616261646162614E504720503250334564676520514569AE69F3656C612C2003
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 302 net.jxta.impl.endpoint.relay.RelayTransport.stopApp()
Relay Message Transport stopped
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 845 net.jxta.impl.endpoint.router.EndpointRouter.stopApp()
urn:jxta:jxta-NetGroup "NetPeerGroup"[0,1434041222] / urn:jxta:jxta-WorldGroup "World PeerGroup"[0,194494468] / [2088051243] : Router Message Transport stopped.
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 590 net.jxta.impl.discovery.DiscoveryServiceImpl.stopApp()
Discovery service stopped.
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 579 net.jxta.impl.rendezvous.edge.EdgePeerRdvService.removeRdv()
Disconnect from RDV urn:jxta:uuid-59616261646162614E5047205032503352656E64657A466FB573A04164656C6103
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 387 net.jxta.impl.endpoint.relay.RelayClient.run()
stop client thread
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 389 net.jxta.impl.rendezvous.RendezVousServiceImpl.stopApp()
Rendezvous Serivce stopped
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 686 net.jxta.impl.endpoint.EndpointServiceImpl.stopApp()
Endpoint Service stopped.
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 202 net.jxta.impl.cm.XIndiceIndexer.close()
Closing Indexer
10 01, 2017 6:16:18 午後 net.jxta.logging.Logging logCheckedInfo
情報: Line 501 net.jxta.platform.NetworkManager.stopNetwork()
Stopped JXTA Network!
10 01, 2017 6:16:21 午後 net.jxta.logging.Logging logCheckedWarning
警告: Line 1059 net.jxta.impl.endpoint.router.EndpointRouter.getGatewayAddress()
getGatewayAddress exception
java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask@642c1ef4 rejected from net.jxta.impl.util.threads.SharedScheduledThreadPoolExecutor@212e9c15[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 15]
Line 2047 java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution()
Line 823 java.util.concurrent.ThreadPoolExecutor.reject()
Line 326 java.util.concurrent.ScheduledThreadPoolExecutor.delayedExecute()
Line 573 java.util.concurrent.ScheduledThreadPoolExecutor.scheduleAtFixedRate()
Line 59 net.jxta.impl.util.threads.SharedScheduledThreadPoolExecutor.scheduleAtFixedRate()
Line 285 net.jxta.impl.endpoint.router.EndpointRouter$ClearPendingQuery.<init>()
Line 1011 net.jxta.impl.endpoint.router.EndpointRouter.getGatewayAddress()
Line 2412 net.jxta.impl.endpoint.router.EndpointRouter.addressMessage()
Line 169 net.jxta.impl.endpoint.router.RouterMessenger.sendMessageBImpl()
Line 789 net.jxta.impl.endpoint.BlockingMessenger.sendIt()
Line 742 net.jxta.impl.endpoint.BlockingMessenger.performDeferredAction()
Line 608 net.jxta.impl.endpoint.BlockingMessenger.sendMessageB()
Line 467 net.jxta.impl.endpoint.EndpointServiceImpl$CanonicalMessenger.sendMessageBImpl()
Line 490 net.jxta.endpoint.ThreadedMessenger.send()
Line 381 net.jxta.endpoint.ThreadedMessenger.run()
Line 745 java.lang.Thread.run()
```
