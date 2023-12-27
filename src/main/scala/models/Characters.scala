import models.Character
case object Characters {
  
  val SpaceMarine = Character(
    characterId = "a617321a-7ad6-11ee-afff-325096b39f47",
    ballisticSkill = 90,
    weaponSkill = 95,
    movement = 4
  )

  val Ork = Character(
    characterId = "a0d58842-7ad6-11ee-b72f-325096b39f47",
    ballisticSkill = 20,
    weaponSkill = 60,
    movement = 2
  )
}
