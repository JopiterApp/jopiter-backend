# Classifier Design

### Temos:

Arquivos CSV onde a primeira coluna representa o nome do item e todas as outras colunas representam
detalhes/categorias/classes dos itens.

### Queremos

Classe de dados abstrata

Nome - Coluna 1 - Coluna 2 - ... - Coluna N

```
val nome: String,
vararg val colunas: Pair<String, String>
```

### Exemplos

CSV:
João,Olhos Castanhos,Cabelo Preto

Tupla:

```
[nome = João, colunas=[Olhos Castanhos, Cabelo Preto]]
```

## Dados do Classificador

### Para treinar:

Receberá todas as tuplas de um determinado grupo. Todas são semelhantes entre si, e possuem a mesma quantidade de
colunas. O classificador só será capaz de comparar tipos que ele já conhece

### Para prever/classificar

Receberá apenas o nome e terá que deduzir as outras tuplas a partir disso

## Implementação do classificador

1. CSV é convertido para N-Tuplas
2. Tuplas passam por tratamento
    1. corpus <= Transformar o nome com BagOfWords (lib smile kotlin nlp bag) passando a lista de stop words (TODO:
       melhorar lista) e um stemmer (FIXME, explicar melhor) de palavras em português
    2. vocabulario <= todos os nomes das tuplas, após passado as stop words e o stemmer
    3. corpusVetorizado <= Com o corpus (nomes como bag of words) e o vocabulário, contar quantas vezes cada vocábulo
       aparece em cada documento, armazenando cada documento
    4. dadosProntosParaModelagem <= Usamos o algorítimo tfidf para melhorar a definição de importância de cada termo de
       cada documento (FIXME, explicar melhor)
3. Com os dados prontos para modelagem, treinamos um modelo diferente para cada uma das N colunas:
    1. Primeiro através de um algorítimo OVR (One Versus Rest) (FIXME explicar melhor OVR)
    2. Este OVR será alimentado com os dados organizados em um vetor de suporte (support vector machine / SVM) (FIXME
       adicionar artigo que explicar porque svm é o melhor)
4. Estes modelos gerados compõem o clasificador, e podem ser exportados para armazenamento (economizando assim
   computação futura). É possível estimar pratos futuros neste classificador, tratando o novo documento da mesma forma
   que os antigos. Dessa forma temos as probabilidades que os modelos dão para cada uma das N colunas