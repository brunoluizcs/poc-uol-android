### Trello 
https://trello.com/b/WjEqf9Sk/poc-uol-android

### Especificações para o ambiente de desenvolvimento

* IDE: Android Studio 3.0.1
* Gradle Versao: 4.1
* Linguagem: Java
* TargetSdkVersion: 26
* Build Tools: 26.1.0


### Especificações Técnicas

Dispositivos com sistema android a partir da versão 4.0 [ICE CREAM SANDWICH](https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#ICE_CREAM_SANDWICH)



### Prosposta 

A proposta do aplicativo é se manter simples exibindo uma tela com a listagem de noticias e uma tela secundaria com o detalhe da noticia selecionada na tela anterior. Como premissa parti do princípio de exibir a listagem de noticias da forma mais rápida sem depender de fatores externos como conexão da internet ou tempo de resposta do servidor, escrevi uma estratégia de cache onde os dados são sincronizados com o servidor e armazenados em um banco de dados local de hora em hora o app sincroniza com o servidor para atualizar o seu banco de dados, essa sincronização ocorre em background mesmo se o app estiver fechado.


<img src="http://bomcodigo.com/poc-uol/device-2018-01-29-194238.png" width="400px" />
<img src="http://bomcodigo.com/poc-uol/device-2018-01-29-194315.png" width="400px" />






