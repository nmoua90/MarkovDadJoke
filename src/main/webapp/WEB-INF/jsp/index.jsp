<!DOCTYPE HTML>
<html>

<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/bootstrap-responsive.css">
</head>

<body>

	<div style="text-align: center;">
		<h1>Markov Generated Dad Jokes!</h1>

		<img style="padding-bottom: 10px;"
			src="https://i.imgur.com/DjBm0Mx.jpg">

		<h3>Dad Joke Scale</h3>

		<form action="/">
			<div>
				Gibberish Jokes...
				<input type="range" min="1" max="7" value="5" name="nGramRange"> 
				Coherent Jokes...
			</div>

			<h2>${dadjoke}</h2>
			
			<button>Generate Dad Joke!</button>
		
		</form>
		
	</div>

</body>
</html>