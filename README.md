# DeliveryLowerCostWm

O sistema de Entregas foi desenvolvido como um Projeto Web utilizando-se de um Web Service Rest, utilizando a 
especificação JAX-RS e Jersey como implementação, web container TomCat 8 e Maven como gerenciador de dependência.
O Web Service foi utilizado com o objetivo de fazer integrações com outros sistemas, adotei a arquitetura Rest por
ser mais simples, leve, não sobrecarregar o servidor, funcionar com o protocolo http, etc. O funcionamento do 
sistema é da seguinte forma:

Exemplo de acesso aos métodos:

Métodos acessados via http:
http://localhost:8080/DeliveryLowerCostWm/maps -> retorna XML contendo todas as Rotas gravadas no banco de dados;
http://localhost:8080/DeliveryLowerCostWm/maps/Sao%20Paulo/A/F/2/3.5 -> retorna XML contendo a Rota mais 
curta, distância e valor total gasto com combustível;

Métodos acessados via curl(ferramenta utilizada para simular requisições http).
POST(cadastro de Rota)
- curl -d <br.com.deliveryLowerCostWm.model.Map><origin>B</origin><distancy>20</distancy>
<name>Sao Paulo</name><destination>C</destination></br.com.deliveryLowerCostWm.model.Map>" 
http://localhost:8080/DeliveryLowerCostWm/maps

DELETE(remoção de Rota)
curl -X DELETE http://localhost:8080/DeliveryLowerCostWm/maps/Sao%20Paulo/F/G

PUT(alteração distância Rota)
curl -v -X PUT -d "<br.com.deliveryLowerCostWm.model.Map><origin>F</origin><distancy>15</distancy><name>Sao Paulo</name><destination>G</destination></br.com.deliveryLowerCostWm.model.Map>" http://localhost:8080/DeliveryLowerCostWm/maps/22

Obs: Todos os comandos não deverão ter quebras de linhas;
Obs: Disponibilizei a opção de update de distância, caso a distância entre dois pontos diminua ou aumente por algum
desvio, obras, etc..


Segue link com maiores informações e com instruções para download do Curl:
link: https://curl.haxx.se
