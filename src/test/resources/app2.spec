* UserScreen
  "Nom:"                @InputText(nom)
  "Genre:"              @Combo(genre)
( "Nom de jeune fille:" @InputText(nomJeuneFille) )- blocJeuneFille
  "Age:"                @InputText(age) "Taille:" @InputText(taille)
  @Button(sauve, "Enregistrer")

enum Genre(Femme, Homme)
entity Personne(nom: String, genre: Genre, nomJeuneFille: String, age: Integer, taille: Integer)
person = @Instance(Person)
person.nom <-> nom.value
person.genre <-> genre.value
person.age <-> age.value
person.taille <-> taille.value
person.nomJeuneFille <-> nomJeuneFille.value
blocJeuneFille.visible = genre.value == Genre.Femme
sauve.action = @Save(person)