package com.lodigital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Dependencia.
 */
@Entity
@Table(name = "dependencia")
public class Dependencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @Size(max = 110)
    @Column(name = "direccion", length = 110)
    private String direccion;

    @Size(max = 150)
    @Column(name = "descripcion", length = 150)
    private String descripcion;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "fecha_modificacion")
    private Instant fechaModificacion;

    @Column(name = "nombre_contacto_comercial")
    private String nombreContactoComercial;

    @Column(name = "cargo_contacto_comercial")
    private String cargoContactoComercial;

    @Column(name = "telefono_principal_contacto_comercial")
    private String telefonoPrincipalContactoComercial;

    @Column(name = "telefono_secundario_contacto_comercial")
    private String telefonoSecundarioContactoComercial;

    @Column(name = "email_contacto_comercial")
    private String emailContactoComercial;

    @Column(name = "nombre_contacto_tecnico")
    private String nombreContactoTecnico;

    @Column(name = "cargo_contacto_tecnico")
    private String cargoContactoTecnico;

    @Column(name = "telefono_principal_contacto_tecnico")
    private String telefonoPrincipalContactoTecnico;

    @Column(name = "telefono_secundario_contacto_tecnico")
    private String telefonoSecundarioContactoTecnico;

    @Column(name = "email_contacto_tecnico")
    private String emailContactoTecnico;

    @OneToMany(mappedBy = "dependencia")
    private Set<UsuarioDependencia> usuarioDependencias = new HashSet<>();

    @OneToMany(mappedBy = "dependenciaMandante")
    private Set<Contrato> contratoes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "dependencias", allowSetters = true)
    private Entidad entidad;

    @ManyToOne
    @JsonIgnoreProperties(value = "dependencias", allowSetters = true)
    private Region region;

    @ManyToOne
    @JsonIgnoreProperties(value = "dependencias", allowSetters = true)
    private Comuna comuna;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Dependencia nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public Dependencia direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Dependencia descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public Dependencia fechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaModificacion() {
        return fechaModificacion;
    }

    public Dependencia fechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getNombreContactoComercial() {
        return nombreContactoComercial;
    }

    public Dependencia nombreContactoComercial(String nombreContactoComercial) {
        this.nombreContactoComercial = nombreContactoComercial;
        return this;
    }

    public void setNombreContactoComercial(String nombreContactoComercial) {
        this.nombreContactoComercial = nombreContactoComercial;
    }

    public String getCargoContactoComercial() {
        return cargoContactoComercial;
    }

    public Dependencia cargoContactoComercial(String cargoContactoComercial) {
        this.cargoContactoComercial = cargoContactoComercial;
        return this;
    }

    public void setCargoContactoComercial(String cargoContactoComercial) {
        this.cargoContactoComercial = cargoContactoComercial;
    }

    public String getTelefonoPrincipalContactoComercial() {
        return telefonoPrincipalContactoComercial;
    }

    public Dependencia telefonoPrincipalContactoComercial(String telefonoPrincipalContactoComercial) {
        this.telefonoPrincipalContactoComercial = telefonoPrincipalContactoComercial;
        return this;
    }

    public void setTelefonoPrincipalContactoComercial(String telefonoPrincipalContactoComercial) {
        this.telefonoPrincipalContactoComercial = telefonoPrincipalContactoComercial;
    }

    public String getTelefonoSecundarioContactoComercial() {
        return telefonoSecundarioContactoComercial;
    }

    public Dependencia telefonoSecundarioContactoComercial(String telefonoSecundarioContactoComercial) {
        this.telefonoSecundarioContactoComercial = telefonoSecundarioContactoComercial;
        return this;
    }

    public void setTelefonoSecundarioContactoComercial(String telefonoSecundarioContactoComercial) {
        this.telefonoSecundarioContactoComercial = telefonoSecundarioContactoComercial;
    }

    public String getEmailContactoComercial() {
        return emailContactoComercial;
    }

    public Dependencia emailContactoComercial(String emailContactoComercial) {
        this.emailContactoComercial = emailContactoComercial;
        return this;
    }

    public void setEmailContactoComercial(String emailContactoComercial) {
        this.emailContactoComercial = emailContactoComercial;
    }

    public String getNombreContactoTecnico() {
        return nombreContactoTecnico;
    }

    public Dependencia nombreContactoTecnico(String nombreContactoTecnico) {
        this.nombreContactoTecnico = nombreContactoTecnico;
        return this;
    }

    public void setNombreContactoTecnico(String nombreContactoTecnico) {
        this.nombreContactoTecnico = nombreContactoTecnico;
    }

    public String getCargoContactoTecnico() {
        return cargoContactoTecnico;
    }

    public Dependencia cargoContactoTecnico(String cargoContactoTecnico) {
        this.cargoContactoTecnico = cargoContactoTecnico;
        return this;
    }

    public void setCargoContactoTecnico(String cargoContactoTecnico) {
        this.cargoContactoTecnico = cargoContactoTecnico;
    }

    public String getTelefonoPrincipalContactoTecnico() {
        return telefonoPrincipalContactoTecnico;
    }

    public Dependencia telefonoPrincipalContactoTecnico(String telefonoPrincipalContactoTecnico) {
        this.telefonoPrincipalContactoTecnico = telefonoPrincipalContactoTecnico;
        return this;
    }

    public void setTelefonoPrincipalContactoTecnico(String telefonoPrincipalContactoTecnico) {
        this.telefonoPrincipalContactoTecnico = telefonoPrincipalContactoTecnico;
    }

    public String getTelefonoSecundarioContactoTecnico() {
        return telefonoSecundarioContactoTecnico;
    }

    public Dependencia telefonoSecundarioContactoTecnico(String telefonoSecundarioContactoTecnico) {
        this.telefonoSecundarioContactoTecnico = telefonoSecundarioContactoTecnico;
        return this;
    }

    public void setTelefonoSecundarioContactoTecnico(String telefonoSecundarioContactoTecnico) {
        this.telefonoSecundarioContactoTecnico = telefonoSecundarioContactoTecnico;
    }

    public String getEmailContactoTecnico() {
        return emailContactoTecnico;
    }

    public Dependencia emailContactoTecnico(String emailContactoTecnico) {
        this.emailContactoTecnico = emailContactoTecnico;
        return this;
    }

    public void setEmailContactoTecnico(String emailContactoTecnico) {
        this.emailContactoTecnico = emailContactoTecnico;
    }

    public Set<UsuarioDependencia> getUsuarioDependencias() {
        return usuarioDependencias;
    }

    public Dependencia usuarioDependencias(Set<UsuarioDependencia> usuarioDependencias) {
        this.usuarioDependencias = usuarioDependencias;
        return this;
    }

    public Dependencia addUsuarioDependencia(UsuarioDependencia usuarioDependencia) {
        this.usuarioDependencias.add(usuarioDependencia);
        usuarioDependencia.setDependencia(this);
        return this;
    }

    public Dependencia removeUsuarioDependencia(UsuarioDependencia usuarioDependencia) {
        this.usuarioDependencias.remove(usuarioDependencia);
        usuarioDependencia.setDependencia(null);
        return this;
    }

    public void setUsuarioDependencias(Set<UsuarioDependencia> usuarioDependencias) {
        this.usuarioDependencias = usuarioDependencias;
    }

    public Set<Contrato> getContratoes() {
        return contratoes;
    }

    public Dependencia contratoes(Set<Contrato> contratoes) {
        this.contratoes = contratoes;
        return this;
    }

    public Dependencia addContrato(Contrato contrato) {
        this.contratoes.add(contrato);
        contrato.setDependenciaMandante(this);
        return this;
    }

    public Dependencia removeContrato(Contrato contrato) {
        this.contratoes.remove(contrato);
        contrato.setDependenciaMandante(null);
        return this;
    }

    public void setContratoes(Set<Contrato> contratoes) {
        this.contratoes = contratoes;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public Dependencia entidad(Entidad entidad) {
        this.entidad = entidad;
        return this;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public Region getRegion() {
        return region;
    }

    public Dependencia region(Region region) {
        this.region = region;
        return this;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public Dependencia comuna(Comuna comuna) {
        this.comuna = comuna;
        return this;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dependencia)) {
            return false;
        }
        return id != null && id.equals(((Dependencia) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dependencia{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", nombreContactoComercial='" + getNombreContactoComercial() + "'" +
            ", cargoContactoComercial='" + getCargoContactoComercial() + "'" +
            ", telefonoPrincipalContactoComercial='" + getTelefonoPrincipalContactoComercial() + "'" +
            ", telefonoSecundarioContactoComercial='" + getTelefonoSecundarioContactoComercial() + "'" +
            ", emailContactoComercial='" + getEmailContactoComercial() + "'" +
            ", nombreContactoTecnico='" + getNombreContactoTecnico() + "'" +
            ", cargoContactoTecnico='" + getCargoContactoTecnico() + "'" +
            ", telefonoPrincipalContactoTecnico='" + getTelefonoPrincipalContactoTecnico() + "'" +
            ", telefonoSecundarioContactoTecnico='" + getTelefonoSecundarioContactoTecnico() + "'" +
            ", emailContactoTecnico='" + getEmailContactoTecnico() + "'" +
            "}";
    }
}
