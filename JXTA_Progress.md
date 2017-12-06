
# 2017/12/06 時点での進捗

* 従来の一対一のPipeを使用する方法だと、一つのEdgeが増加すると、RDVのコードが数十行
  増加してしまう。ここで、MultiCast以外にもPropagate Pipeがあるらしいので、それを試そうと考えているが、サンプルも何もないところからするのは厳しいので、今他の資料も探している。
* 今まで使用していたUnicastPipeから、PropagatePipeなるものを使えば複数のPeerと通信ができそうだが、まだ、通信に成功していない
* 古い資料しかなく、しかも多くの資料は市販の本の販促用の部分抜き

# 課題

* とにかく実装

# 2017/11/24 時点の進捗

* なし

# 課題

* 一つのRDVに対して複数(10くらい？)のEdgeをつけて、RDVが受け取ったクエリをそれぞれEdgeの持っているDBに投げて、その結果をUnionして、RDVのDBに格納する。

# 2017/11/17 時点の進捗
* 一つのdockerコンテナに２つのデータベースを立ててた。
* MulticastSocketをちょっと試してみたがまだうまく行ってない。
# 課題

* 通信の初めはブロードキャストしているから、Edgeはベタ書きでもいいはず
* ただし、RDVのIPは今回はベタ書きでいい

# 2017/11/8 時点の進捗

* 先週2つのPeerの間でデータをやり取りすることはできたが、ここから3つ以上でやり取りするためにどの方法がいいかをまだ確認している途中(p44まで読んだ。このあたりもう少し目を通しておきたい)
* p.139-p.150も読みたい
* 先週JXTAで定義されているPipeクラスを使った通信から、Socketを使用するとPeerGroupに対するマルチキャストができるようなので、Socketを使用した通信に変更しようか検討するとしたが、PeerGroup等に対してまだ確認している段階
* Edgeは把握しているRendezVousにどれか一つと通信できるまでアクセスを試みるので、RendezVousの一覧DBが必要か？RDVがネットワーク起動したら自分のIPとPortをDBにInsertするみたいな感じかな？
* 前にできた通信の手順からMessageBoxを排除したい
* RDVとEdge間での通信はできるとして、RDVとRDV間での通信はできるのか？Multicastでなんとかなるの？
* 初めの通信を行う時に、受信待ちをしているRDVをDBに挿入しておいて、EDGEはそこに見に行くのではどうか？（RDVはDHCPでIP変わる？）

# 2017/11/1 時点の進捗

* 双方向にクエリを送り合って、相手の接続しているDBからのクエリ結果を取得するところまでは成功
  (ただし、コンソールがシステムのログと同じになってしまっていて、ログが吐かれるとストリームのバッファが
   クリアされるっぽいから全力でSQLを書く必要あり)
* 現時点では多ノードでの通信を試みているところ
* 現時点で一つのRandezVousに対して複数のEdgeをつなげて通信することはできそう
* データ送信でSocketとか使った方法で同じPeerGroupの中のPeerと通信できたりするっぽいので確認が必要(JXTAのソケットはJavaのものと大体同じらしい)

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
