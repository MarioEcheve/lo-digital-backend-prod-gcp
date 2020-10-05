package com.lodigital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Archivo.
 */
@Entity
@Table(name = "archivo")
public class Archivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    
    @Lob
    @Column(name = "archivo", nullable = false)
    private byte[] archivo;

    @Column(name = "archivo_content_type", nullable = false)
    private String archivoContentType;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @NotNull
    @Column(name = "size", nullable = false)
    private String size;

    @Size(max = 200)
    @Column(name = "nombre", length = 200)
    private String nombre;

    @Size(max = 200)
    @Column(name = "url_archivo", length = 200)
    private String urlArchivo;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JsonIgnoreProperties(value = "archivos", allowSetters = true)
    private Folio folio;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public Archivo archivo(byte[] archivo) {
        this.archivo = archivo;
        return this;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String getArchivoContentType() {
        return archivoContentType;
    }

    public Archivo archivoContentType(String archivoContentType) {
        this.archivoContentType = archivoContentType;
        return this;
    }

    public void setArchivoContentType(String archivoContentType) {
        this.archivoContentType = archivoContentType;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Archivo descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSize() {
        return size;
    }

    public Archivo size(String size) {
        this.size = size;
        return this;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getNombre() {
        return nombre;
    }

    public Archivo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public Archivo urlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
        return this;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public Boolean isStatus() {
        return status;
    }

    public Archivo status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Folio getFolio() {
        return folio;
    }

    public Archivo folio(Folio folio) {
        this.folio = folio;
        return this;
    }

    public void setFolio(Folio folio) {
        this.folio = folio;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Archivo)) {
            return false;
        }
        return id != null && id.equals(((Archivo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Archivo{" +
            "id=" + getId() +
            ", archivo='" + getArchivo() + "'" +
            ", archivoContentType='" + getArchivoContentType() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", size='" + getSize() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", urlArchivo='" + getUrlArchivo() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
