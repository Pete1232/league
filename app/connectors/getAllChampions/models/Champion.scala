package connectors.getAllChampions.models

case class Champion(rankedPlayEnabled: Boolean,
                    botEnabled: Boolean,
                    botMmEnabled: Boolean,
                    active: Boolean,
                    freeToPlay: Boolean,
                    id: Long)
