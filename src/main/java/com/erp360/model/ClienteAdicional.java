package com.erp360.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name = "cliente_adicional", schema = "public")
public class ClienteAdicional  implements Serializable {

	private static final long serialVersionUID = 1419889919632323263L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	//------ GARANTE
	@Column(name="nombre_garante",nullable=true )
	private String nombreGarante;
	
	@Column(name="ci_garante",nullable=true )
	private String ciGarante;
	
	@Column(name="direccion_garante",nullable=true )
	private String direccionGarante;

	// ---- DATOS PERSONALES ----

	@Column(name="nombre_compl_hijo_1",nullable=true )
	private String nombreCompletoHijo1;

	@Column(name="edad_hijo_1",nullable=true )
	private Integer edadHijo1;

	@Column(name="telf_hijo_1",nullable=true )
	private String telfHijo1;

	@Column(name="colegio_estudio_hijo_1",nullable=true )
	private String colegioEstudioHijo1;

	@Column(name="dir_colegio_hijo_1",nullable=true )
	private String direccionColegioHijo1;

	@Column(name="telf_colegio_hijo_1",nullable=true )
	private String telefonoColegioHijo1;

	@Column(name="nombre_compl_hijo_2",nullable=true )
	private String nombreCompletoHijo2;

	@Column(name="edad_hijo_2",nullable=true )
	private Integer edadHijo2;

	@Column(name="telf_hijo_2",nullable=true )
	private String telfHijo2;

	@Column(name="colegio_estudio_hijo_2",nullable=true )
	private String colegioEstudioHijo2;

	@Column(name="dir_colegio_hijo_2",nullable=true )
	private String direccionColegioHijo2;

	@Column(name="telf_colegio_hijo_2",nullable=true )
	private String telefonoColegioHijo2;

	@Column(name="nombre_comp_cony",nullable=true )
	private String nombreCompletoConyugue;

	@Column(name="telf_cony",nullable=true )
	private String telefonoConyugue;

	@Column(name="empresa_cony",nullable=true )
	private String empresaConyugue;

	@Column(name="dir_laboral_cony",nullable=true )
	private String direccionLaboralConyugue;

	@Column(name="antig_laboral_cony",nullable=true )
	private String antiguedadLaboralConyugue;

	@Column(name="telf_empresa_cony",nullable=true )
	private String telefonoEmpresaConyugue;

	@Column(name="correo_cony",nullable=true )
	private String correoConyugue;

	// ---- DATOS LABORALES ----

	@Column(name="empresa_laboral",nullable=true )
	private String empresaLaboral;

	@Column(name="telf_laboral",nullable=true )
	private String telefonoLaboral;

	@Column(name="dir_laboral",nullable=true )
	private String direccionLaboral;

	@Column(name="cargo_laboral",nullable=true )
	private String cargoLaboral;

	@Column(name="area_laboral",nullable=true )
	private String areaLaboral;

	@Column(name="antiguedad_laboral",nullable=true )
	private String antiguedadLaboral;

	@Column(name="nom_compl_colega_1_lab",nullable=true )
	private String nombreCompletoColega1Laboral;

	@Column(name="telf_colega_1_lab",nullable=true )
	private String telefonoColega1Laboral;

	@Column(name="dir_colega_1_lab",nullable=true )
	private String direccionColega1Laboral;

	@Column(name="nom_compl_colega_2_lab",nullable=true )
	private String nombreCompletoColega2Laboral;

	@Column(name="telf_colega_2_lab",nullable=true )
	private String telefonoColega2Laboral;

	@Column(name="dir_colega_2_lab",nullable=true )
	private String direccionColega2Laboral;

	// ---- INFORMACION ADICIONAL ----

	@Column(name="nom_compl_padre",nullable=true )
	private String nombreCompletoPadre;

	@Column(name="telf_padre",nullable=true )
	private String telefonoPadre;

	@Column(name="dir_padre",nullable=true )
	private String direccionPadre;

	@Column(name="nom_compl_madre",nullable=true )
	private String nombreCompletoMadre;

	@Column(name="telf_madre",nullable=true )
	private String telefonoMadre;

	@Column(name="dir_madre",nullable=true )
	private String direccionMadre;

	@Column(name="nom_compl_hermano_1",nullable=true )
	private String nombreCompletoHermano1;

	@Column(name="telf_hermano_1",nullable=true )
	private String telefonotoHermano1;

	@Column(name="dir_hermano_1",nullable=true )
	private String direccionHermano1;

	@Column(name="nom_compl_hermano_2",nullable=true )
	private String nombreCompletoHermano2;

	@Column(name="telf_hermano_2",nullable=true )
	private String telefonotoHermano2;

	@Column(name="dir_hermano_2",nullable=true )
	private String direccionHermano2;

	// ---- UBICACION ----
	@Column(name="ubicacion_titulo",nullable=true )
	private String ubicacionTitulo;

	@Column(name="ubicacion_latitud",nullable=true )
	private double ubicacionLatitud;

	@Column(name="ubicacion_longitud",nullable=true )
	private double ubicacionLongitud;

	/// ---------
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_cliente", nullable=false)
	private Cliente cliente;

	@Column(name = "foto", nullable = true)
	private byte[] foto;

	@Column(name = "peso_foto", nullable = true)
	private int pesoFoto;

	//CAMPOS NECESARIOS

	@Size(max = 2) //AC , IN
	private String estado;

	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;

	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;

	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;

	public ClienteAdicional() {
		super();
		this.id= 0 ;
		this.estado="AC";
		this.setFechaRegistro(new Date());
		this.ubicacionLatitud = -17.781291;
		this.ubicacionLongitud = -63.166525;
		this.pesoFoto = 0;

	}

	@Override
	public String toString() {
		return "ClienteAdicional [id=" + id + ", nombreCompletoHijo1="
				+ nombreCompletoHijo1 + ", edadHijo1=" + edadHijo1
				+ ", telfHijo1=" + telfHijo1 + ", colegioEstudioHijo1="
				+ colegioEstudioHijo1 + ", direccionColegioHijo1="
				+ direccionColegioHijo1 + ", telefonoColegioHijo1="
				+ telefonoColegioHijo1 + ", nombreCompletoHijo2="
				+ nombreCompletoHijo2 + ", edadHijo2=" + edadHijo2
				+ ", telfHijo2=" + telfHijo2 + ", colegioEstudioHijo2="
				+ colegioEstudioHijo2 + ", direccionColegioHijo2="
				+ direccionColegioHijo2 + ", telefonoColegioHijo2="
				+ telefonoColegioHijo2 + ", nombreCompletoConyugue="
				+ nombreCompletoConyugue + ", telefonoConyugue="
				+ telefonoConyugue + ", direccionLaboralConyugue="
				+ direccionLaboralConyugue + ", antiguedadLaboralConyugue="
				+ antiguedadLaboralConyugue + ", telefonoEmpresaConyugue="
				+ telefonoEmpresaConyugue + ", correoConyugue="
				+ correoConyugue + ", empresaLaboral=" + empresaLaboral
				+ ", telefonoLaboral=" + telefonoLaboral
				+ ", direccionLaboral=" + direccionLaboral + ", cargoLaboral="
				+ cargoLaboral + ", areaLaboral=" + areaLaboral
				+ ", antiguedadLaboral=" + antiguedadLaboral
				+ ", nombreCompletoColega1Laboral="
				+ nombreCompletoColega1Laboral + ", telefonoColega1Laboral="
				+ telefonoColega1Laboral + ", direccionColega1Laboral="
				+ direccionColega1Laboral + ", nombreCompletoColega2Laboral="
				+ nombreCompletoColega2Laboral + ", telefonoColega2Laboral="
				+ telefonoColega2Laboral + ", direccionColega2Laboral="
				+ direccionColega2Laboral + ", nombreCompletoPadre="
				+ nombreCompletoPadre + ", telefonoPadre=" + telefonoPadre
				+ ", direccionPadre=" + direccionPadre
				+ ", nombreCompletoMadre=" + nombreCompletoMadre
				+ ", telefonoMadre=" + telefonoMadre + ", direccionMadre="
				+ direccionMadre + ", nombreCompletoHermano1="
				+ nombreCompletoHermano1 + ", telefonotoHermano1="
				+ telefonotoHermano1 + ", direccionHermano1="
				+ direccionHermano1 + ", nombreCompletoHermano2="
				+ nombreCompletoHermano2 + ", telefonotoHermano2="
				+ telefonotoHermano2 + ", direccionHermano2="
				+ direccionHermano2 + ", ubicacionTitulo=" + ubicacionTitulo
				+ ", ubicacionLatitud=" + ubicacionLatitud
				+ ", ubicacionLongitud=" + ubicacionLongitud + ", estado="
				+ estado + ", fechaRegistro=" + fechaRegistro
				+ ", fechaModificacion=" + fechaModificacion
				+ ", usuarioRegistro=" + usuarioRegistro + "]";
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}else{
			if(!(obj instanceof ClienteAdicional)){
				return false;
			}else{
				if(((ClienteAdicional)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public double getUbicacionLatitud() {
		return ubicacionLatitud;
	}

	public void setUbicacionLatitud(double ubicacionLatitud) {
		this.ubicacionLatitud = ubicacionLatitud;
	}

	public double getUbicacionLongitud() {
		return ubicacionLongitud;
	}

	public void setUbicacionLongitud(double ubicacionLongitud) {
		this.ubicacionLongitud = ubicacionLongitud;
	}

	public String getUbicacionTitulo() {
		return ubicacionTitulo;
	}

	public void setUbicacionTitulo(String ubicacionTitulo) {
		this.ubicacionTitulo = ubicacionTitulo;
	}

	public String getNombreCompletoHijo1() {
		return nombreCompletoHijo1;
	}

	public void setNombreCompletoHijo1(String nombreCompletoHijo1) {
		this.nombreCompletoHijo1 = nombreCompletoHijo1;
	}

	public Integer getEdadHijo1() {
		return edadHijo1;
	}

	public void setEdadHijo1(Integer edadHijo1) {
		this.edadHijo1 = edadHijo1;
	}

	public String getTelfHijo1() {
		return telfHijo1;
	}

	public void setTelfHijo1(String telfHijo1) {
		this.telfHijo1 = telfHijo1;
	}

	public String getColegioEstudioHijo1() {
		return colegioEstudioHijo1;
	}

	public void setColegioEstudioHijo1(String colegioEstudioHijo1) {
		this.colegioEstudioHijo1 = colegioEstudioHijo1;
	}

	public String getDireccionColegioHijo1() {
		return direccionColegioHijo1;
	}

	public void setDireccionColegioHijo1(String direccionColegioHijo1) {
		this.direccionColegioHijo1 = direccionColegioHijo1;
	}

	public String getTelefonoColegioHijo1() {
		return telefonoColegioHijo1;
	}

	public void setTelefonoColegioHijo1(String telefonoColegioHijo1) {
		this.telefonoColegioHijo1 = telefonoColegioHijo1;
	}

	public String getNombreCompletoHijo2() {
		return nombreCompletoHijo2;
	}

	public void setNombreCompletoHijo2(String nombreCompletoHijo2) {
		this.nombreCompletoHijo2 = nombreCompletoHijo2;
	}

	public Integer getEdadHijo2() {
		return edadHijo2;
	}

	public void setEdadHijo2(Integer edadHijo2) {
		this.edadHijo2 = edadHijo2;
	}

	public String getTelfHijo2() {
		return telfHijo2;
	}

	public void setTelfHijo2(String telfHijo2) {
		this.telfHijo2 = telfHijo2;
	}

	public String getColegioEstudioHijo2() {
		return colegioEstudioHijo2;
	}

	public void setColegioEstudioHijo2(String colegioEstudioHijo2) {
		this.colegioEstudioHijo2 = colegioEstudioHijo2;
	}

	public String getDireccionColegioHijo2() {
		return direccionColegioHijo2;
	}

	public void setDireccionColegioHijo2(String direccionColegioHijo2) {
		this.direccionColegioHijo2 = direccionColegioHijo2;
	}

	public String getTelefonoColegioHijo2() {
		return telefonoColegioHijo2;
	}

	public void setTelefonoColegioHijo2(String telefonoColegioHijo2) {
		this.telefonoColegioHijo2 = telefonoColegioHijo2;
	}

	public String getNombreCompletoConyugue() {
		return nombreCompletoConyugue;
	}

	public void setNombreCompletoConyugue(String nombreCompletoConyugue) {
		this.nombreCompletoConyugue = nombreCompletoConyugue;
	}

	public String getTelefonoConyugue() {
		return telefonoConyugue;
	}

	public void setTelefonoConyugue(String telefonoConyugue) {
		this.telefonoConyugue = telefonoConyugue;
	}

	public String getDireccionLaboralConyugue() {
		return direccionLaboralConyugue;
	}

	public void setDireccionLaboralConyugue(String direccionLaboralConyugue) {
		this.direccionLaboralConyugue = direccionLaboralConyugue;
	}

	public String getAntiguedadLaboralConyugue() {
		return antiguedadLaboralConyugue;
	}

	public void setAntiguedadLaboralConyugue(String antiguedadLaboralConyugue) {
		this.antiguedadLaboralConyugue = antiguedadLaboralConyugue;
	}

	public String getTelefonoEmpresaConyugue() {
		return telefonoEmpresaConyugue;
	}

	public void setTelefonoEmpresaConyugue(String telefonoEmpresaConyugue) {
		this.telefonoEmpresaConyugue = telefonoEmpresaConyugue;
	}

	public String getCorreoConyugue() {
		return correoConyugue;
	}

	public void setCorreoConyugue(String correoConyugue) {
		this.correoConyugue = correoConyugue;
	}

	public String getEmpresaLaboral() {
		return empresaLaboral;
	}

	public void setEmpresaLaboral(String empresaLaboral) {
		this.empresaLaboral = empresaLaboral;
	}

	public String getTelefonoLaboral() {
		return telefonoLaboral;
	}

	public void setTelefonoLaboral(String telefonoLaboral) {
		this.telefonoLaboral = telefonoLaboral;
	}

	public String getDireccionLaboral() {
		return direccionLaboral;
	}

	public void setDireccionLaboral(String direccionLaboral) {
		this.direccionLaboral = direccionLaboral;
	}

	public String getCargoLaboral() {
		return cargoLaboral;
	}

	public void setCargoLaboral(String cargoLaboral) {
		this.cargoLaboral = cargoLaboral;
	}

	public String getAreaLaboral() {
		return areaLaboral;
	}

	public void setAreaLaboral(String areaLaboral) {
		this.areaLaboral = areaLaboral;
	}

	public String getAntiguedadLaboral() {
		return antiguedadLaboral;
	}

	public void setAntiguedadLaboral(String antiguedadLaboral) {
		this.antiguedadLaboral = antiguedadLaboral;
	}

	public String getNombreCompletoColega1Laboral() {
		return nombreCompletoColega1Laboral;
	}

	public void setNombreCompletoColega1Laboral(String nombreCompletoColega1Laboral) {
		this.nombreCompletoColega1Laboral = nombreCompletoColega1Laboral;
	}

	public String getTelefonoColega1Laboral() {
		return telefonoColega1Laboral;
	}

	public void setTelefonoColega1Laboral(String telefonoColega1Laboral) {
		this.telefonoColega1Laboral = telefonoColega1Laboral;
	}

	public String getDireccionColega1Laboral() {
		return direccionColega1Laboral;
	}

	public void setDireccionColega1Laboral(String direccionColega1Laboral) {
		this.direccionColega1Laboral = direccionColega1Laboral;
	}

	public String getNombreCompletoColega2Laboral() {
		return nombreCompletoColega2Laboral;
	}

	public void setNombreCompletoColega2Laboral(String nombreCompletoColega2Laboral) {
		this.nombreCompletoColega2Laboral = nombreCompletoColega2Laboral;
	}

	public String getTelefonoColega2Laboral() {
		return telefonoColega2Laboral;
	}

	public void setTelefonoColega2Laboral(String telefonoColega2Laboral) {
		this.telefonoColega2Laboral = telefonoColega2Laboral;
	}

	public String getDireccionColega2Laboral() {
		return direccionColega2Laboral;
	}

	public void setDireccionColega2Laboral(String direccionColega2Laboral) {
		this.direccionColega2Laboral = direccionColega2Laboral;
	}

	public String getNombreCompletoPadre() {
		return nombreCompletoPadre;
	}

	public void setNombreCompletoPadre(String nombreCompletoPadre) {
		this.nombreCompletoPadre = nombreCompletoPadre;
	}

	public String getTelefonoPadre() {
		return telefonoPadre;
	}

	public void setTelefonoPadre(String telefonoPadre) {
		this.telefonoPadre = telefonoPadre;
	}

	public String getDireccionPadre() {
		return direccionPadre;
	}

	public void setDireccionPadre(String direccionPadre) {
		this.direccionPadre = direccionPadre;
	}

	public String getNombreCompletoMadre() {
		return nombreCompletoMadre;
	}

	public void setNombreCompletoMadre(String nombreCompletoMadre) {
		this.nombreCompletoMadre = nombreCompletoMadre;
	}

	public String getTelefonoMadre() {
		return telefonoMadre;
	}

	public void setTelefonoMadre(String telefonoMadre) {
		this.telefonoMadre = telefonoMadre;
	}

	public String getDireccionMadre() {
		return direccionMadre;
	}

	public void setDireccionMadre(String direccionMadre) {
		this.direccionMadre = direccionMadre;
	}

	public String getNombreCompletoHermano1() {
		return nombreCompletoHermano1;
	}

	public void setNombreCompletoHermano1(String nombreCompletoHermano1) {
		this.nombreCompletoHermano1 = nombreCompletoHermano1;
	}

	public String getTelefonotoHermano1() {
		return telefonotoHermano1;
	}

	public void setTelefonotoHermano1(String telefonotoHermano1) {
		this.telefonotoHermano1 = telefonotoHermano1;
	}

	public String getDireccionHermano1() {
		return direccionHermano1;
	}

	public void setDireccionHermano1(String direccionHermano1) {
		this.direccionHermano1 = direccionHermano1;
	}

	public String getNombreCompletoHermano2() {
		return nombreCompletoHermano2;
	}

	public void setNombreCompletoHermano2(String nombreCompletoHermano2) {
		this.nombreCompletoHermano2 = nombreCompletoHermano2;
	}

	public String getTelefonotoHermano2() {
		return telefonotoHermano2;
	}

	public void setTelefonotoHermano2(String telefonotoHermano2) {
		this.telefonotoHermano2 = telefonotoHermano2;
	}

	public String getDireccionHermano2() {
		return direccionHermano2;
	}

	public void setDireccionHermano2(String direccionHermano2) {
		this.direccionHermano2 = direccionHermano2;
	}

	public String getEmpresaConyugue() {
		return empresaConyugue;
	}

	public void setEmpresaConyugue(String empresaConyugue) {
		this.empresaConyugue = empresaConyugue;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public int getPesoFoto() {
		return pesoFoto;
	}

	public void setPesoFoto(int pesoFoto) {
		this.pesoFoto = pesoFoto;
	}

	public String getNombreGarante() {
		return nombreGarante;
	}

	public void setNombreGarante(String nombreGarante) {
		this.nombreGarante = nombreGarante;
	}

	public String getCiGarante() {
		return ciGarante;
	}

	public void setCiGarante(String ciGarante) {
		this.ciGarante = ciGarante;
	}

	public String getDireccionGarante() {
		return direccionGarante;
	}

	public void setDireccionGarante(String direccionGarante) {
		this.direccionGarante = direccionGarante;
	}

}


