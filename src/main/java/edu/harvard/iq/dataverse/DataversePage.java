/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.NoResultException;

/**
 *
 * @author gdurand
 */
@ViewScoped
@Named("DataversePage")
public class DataversePage implements java.io.Serializable {

    @EJB
    DataverseServiceBean dataverseService;

    private Dataverse dataverse = new Dataverse();
    private boolean editMode = false;
    private Long ownerId;

    public Dataverse getDataverse() {
        return dataverse;
    }

    public void setDataverse(Dataverse dataverse) {
        this.dataverse = dataverse;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void init() {
        if (dataverse.getId() != null) { // view mode for a dataverse           
            dataverse = dataverseService.find(dataverse.getId());

        } else if (ownerId != null) { // create mode for a new child dataverse
            editMode = true;
            dataverse.setOwner(dataverseService.find(ownerId));

        } else { // view mode for root dataverse (or create root dataverse)
            try {
                dataverse = dataverseService.findRootDataverse();
            } catch (EJBException e) {
                if (e.getCause() instanceof NoResultException) {
                    editMode = true;
                } else {
                    throw e;
                }

            }
        }
    }

    public void edit(ActionEvent e) {
        editMode = true;
    }

    public void save(ActionEvent e) {
        dataverse = dataverseService.save(dataverse);
        editMode = false;
    }

    public void cancel(ActionEvent e) {
        dataverse = dataverseService.find(dataverse.getId()); // reset dv values
        editMode = false;
    }
}
