# Bear Bouncer - Serveur d'identification

by Ran Bao & Anthony Pena


## création d'un token

```
/auth
{
	imei: <imei>,
	apiKey: apiKey
}
```

### Réponse ok (http 302)
```
{
	token: <token>,
	callback: <callback>
}
```

### Réponse erreur

#### Requête avec un IMEI mal-formé (http 400)
```
{
	error: "Content is not a well formed IMEI."
}
```

#### Requête avec une apiKey invalide (http 400)
```
{
	error: "ApiKey '<apiKey>' does not exist."
}
```

#### IMEI qui n'existe pas (http 404)
```
{
	error: "No identity corresponds to IMEI '<imei>'"
}
```




## vérification d'une identité

```
/token
{
	token: <token>
}
```

### Réponse ok (http 200)
```
{
	"firstname":"John",
	"lastname":"Doe"
}
```

### Réponse erreur

#### Requête avec un token mal-formé (http 400)
```
{
	error: "Content is not a well formed token."
}
```

#### Token qui n'existe pas (http 404)
```
{
	error: "Not a valid or existing token."
}
```

