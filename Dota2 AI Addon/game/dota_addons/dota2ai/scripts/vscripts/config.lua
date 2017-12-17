-- Set the enemy mid hero. The game may not have behaviours for every hero. The player bot will be picked using a JSON query
Dota2AI.useEnemyHero = true
Dota2AI.enemyHero = "npc_dota_hero_lycan"
Dota2AI.enemyHeroDifficulty = "hard" -- {"passive", "easy", "normal", "hard", "unfair"}

Dota2AI.baseURL = "http://localhost:8080/Dota2AIService/api/service"

-- Set this to 0 to skip the configuration dialog
Dota2AI.ConfigUITimeout = 0