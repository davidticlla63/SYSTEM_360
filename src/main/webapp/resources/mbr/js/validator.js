/**
 * @author mauriciobejaranorivera
 */

/**
 * Funcion que no permite la tecla de espacio
 */
function validateNoSpace(e){
	tecla = (document.all) ? e.keyCode : e.which;
	//Tecla de retroceso para borrar, siempre la permite
	if (tecla==8){
		return true;
	}
	//espacio=32
	if(tecla==32){
		return false;
	}
}


function autoCompleteLostFocus(autocompleteId, comand) {

    if ($("[id='" + autocompleteId + "_input']").val().trim() == "") {
        eval(comando);
    }

}
/** Funcion que reemplaza (borra) los espacios en blanco del inicio
 * y final de una cadena. Ejemplo de uso:
 *    la cadena ”  hola  ” al usar la función trim(”  hola  “);
 *     resultaría “hola”.
 */
function trimSpaceBeginAndEnd(e){
	value = e.text;
	alert("tecla: "+value);
	return false;
    //tecla = (document.all) ? e.keyCode : e.which;

	//Tecla de retroceso para borrar, siempre la permite
	//if (tecla==8){
	//	return true;
	//}
	// Patron de entrada, en este caso solo acepta espacios entre palabras
	//patron =/^\s+|\s+$/g;
	//tecla_final = String.fromCharCode(tecla);
	//return patron.test(tecla_final);
}

/** Función que valida si la cadena contiene solo números.
 *  Ejemplo de uso:
 *   Al usar validaSoloNumerico(“1234”) : retorna True.
 *   Al usar validaSoloNumerico(“a123”) : retorna False.
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

/** Función que valida si la cadena contiene solo texto.
 *  Ejemplo de uso:
 *   Al usar validaSoloTexto(“abcd”) : retorna True.
 *   Al usar validaSoloTexto(“1abc”) : retorna False.
 */
function validaSoloTexto(cadena){
  var patron = /^[a-zA-Z]*$/;
  // En caso de querer validar cadenas con espacios usar: /^[a-zA-Z\s]*$/
  if(!cadena.search(patron))
    return true;
  else
    return false;
}

function soloLetras(e){
    key = e.keyCode || e.which;
    tecla = String.fromCharCode(key).toString();
    letras = " áéíóúabcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    especiales = "8-37-39-46";
    tecla_especial = false
    for(var i in especiales){
         if(key == especiales[i]){
             tecla_especial = true;
             break;
         }
     }
     if(letras.indexOf(tecla)==-1 && !tecla_especial){
         return false;
     }
 }

/** Función que valida que la entrada solo sea valores de precios
 */
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


function NumCheck(e, field) {
	  key = e.keyCode ? e.keyCode : e.which
	  // backspace
	  if (key == 8) return true
	  // 0-9
	  if (key > 47 && key < 58) {
	    if (field.value == "") return true
	    regexp = /.[0-9]{2}$/
	    return !(regexp.test(field.value))
	  }
	  // .
	  if (key == 44) {
	    if (field.value == "") return false
	    regexp = /^[0-9]+$/
	    return regexp.test(field.value)
	  }
	  // other key
	  return false
	 
	}
