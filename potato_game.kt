import java.util.concurrent.TimeUnit

class Resource(var amount: Int, val maxAmount: Int) {
    fun add(amount: Int){ this.amount = minOf(maxAmount, this.amount + amount)}
    fun use(amount: Int): Boolean {
        if (this.amount >= amount) {
            this.amount -= amount
            return true
        }
        return false
    }
}

class Building(val type: String, val cost: Int, val capacity:Int) {
    var built: Boolean = false
}


class PotatoField(val size: Int) {
    var potatoesPlanted: Int = 0
    var potatoesHarvested: Int = 0
    var growthTime: Long = 0
    val maxGrowthTime = TimeUnit.SECONDS.toMillis(60)
    var water: Resource = Resource(100, 100)
    var fertilizer: Resource = Resource(50, 100)
    val barn = Building("Barn", 100, 100)


    fun plantPotatoes(amount: Int) {
        if (amount > size - potatoesPlanted) return
        if (!barn.built || barn.capacity < potatoesPlanted + amount) return
        potatoesPlanted += amount
        growthTime = 0
    }

    fun harvestPotatoes() {
        if (potatoesPlanted > 0){
            potatoesHarvested += potatoesPlanted
            potatoesPlanted = 0
        }
    }

    fun update(deltaTime: Long) {
        if (potatoesPlanted > 0) {
            growthTime += deltaTime
            if (growthTime >= maxGrowthTime) {
                harvestPotatoes()
                growthTime = 0
            }
        }
    }

    fun water(amount: Int): Boolean = water.use(amount)
    fun fertilize(amount: Int): Boolean = fertilizer.use(amount)
}

fun main() {
    val field = PotatoField(10)
    field.plantPotatoes(5)
    field.barn.built = true // Строим амбар

    // Имитация игрового цикла
    var lastTime = System.currentTimeMillis()
    while (true) {
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastTime
        lastTime = currentTime

        field.update(deltaTime)
        println("Посажено: ${field.potatoesPlanted}, Собранно: ${field.potatoesHarvested}, Вода: ${field.water.amount}, Удобрения: ${field.fertilizer.amount}, Амбар: ${field.barn.built}")

        Thread.sleep(1000)
    }
}

