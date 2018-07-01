/**
 * @author mauriciobejaranorivera
 */
function validateOnlyNumber(e){
	tecla = (document.all) ? e.keyCode : e.which;

	//Tecla de retroceso para borrar, siempre la permite
	if (tecla==8){
		return true;
	}
	// Patron de entrada, en este caso solo acepta numeros
	patron =/[0-9]/;
	tecla_final = String.fromCharCode(tecla);
	return patron.test(tecla_final);
}

//permite solo numeros y puto decinmal
function validateOnlyPrecio(e){
	tecla = (document.all) ? e.keyCode : e.which;
	 
	// 8 - borrar
	//Tecla de retroceso para borrar, siempre la permite
	if (tecla==8){
		return true;
	}
	// 44 - coma | 46 - punto | 48 - 57  numeros
	if ((tecla == 44) || (tecla == 46) || (tecla >= 48 & tecla <= 57)){
		return true;
	}
	return false;
}

/**
13 enter
48 '0' 
57 '9'
8  borrar
58-57 numeros
46 del punto
44 de la coma 
45 del guión
 */
