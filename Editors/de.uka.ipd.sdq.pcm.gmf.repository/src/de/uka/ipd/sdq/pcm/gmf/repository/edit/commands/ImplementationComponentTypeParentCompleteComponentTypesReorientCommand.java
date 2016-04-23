/*
 * Copyright 2007, IPD, SDQ, University of Karlsruhe
 */
package de.uka.ipd.sdq.pcm.gmf.repository.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

import de.uka.ipd.sdq.pcm.gmf.repository.edit.policies.PalladioComponentModelBaseItemSemanticEditPolicy;
import org.palladiosimulator.pcm.repository.CompleteComponentType;
import org.palladiosimulator.pcm.repository.ImplementationComponentType;

/**
 * @generated
 */
public class ImplementationComponentTypeParentCompleteComponentTypesReorientCommand extends EditElementCommand {

    /**
     * @generated
     */
    private final int reorientDirection;

    /**
     * @generated
     */
    private final EObject referenceOwner;

    /**
     * @generated
     */
    private final EObject oldEnd;

    /**
     * @generated
     */
    private final EObject newEnd;

    /**
     * @generated
     */
    public ImplementationComponentTypeParentCompleteComponentTypesReorientCommand(
            ReorientReferenceRelationshipRequest request) {
        super(request.getLabel(), null, request);
        reorientDirection = request.getDirection();
        referenceOwner = request.getReferenceOwner();
        oldEnd = request.getOldRelationshipEnd();
        newEnd = request.getNewRelationshipEnd();
    }

    /**
     * @generated
     */
    public boolean canExecute() {
        if (false == referenceOwner instanceof ImplementationComponentType) {
            return false;
        }
        if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
            return canReorientSource();
        }
        if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
            return canReorientTarget();
        }
        return false;
    }

    /**
     * @generated
     */
    protected boolean canReorientSource() {
        if (!(oldEnd instanceof CompleteComponentType && newEnd instanceof ImplementationComponentType)) {
            return false;
        }
        return PalladioComponentModelBaseItemSemanticEditPolicy.getLinkConstraints()
                .canExistImplementationComponentTypeParentCompleteComponentTypes_4103(getNewSource(), getOldTarget());
    }

    /**
     * @generated
     */
    protected boolean canReorientTarget() {
        if (!(oldEnd instanceof CompleteComponentType && newEnd instanceof CompleteComponentType)) {
            return false;
        }
        return PalladioComponentModelBaseItemSemanticEditPolicy.getLinkConstraints()
                .canExistImplementationComponentTypeParentCompleteComponentTypes_4103(getOldSource(), getNewTarget());
    }

    /**
     * @generated
     */
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        if (!canExecute()) {
            throw new ExecutionException("Invalid arguments in reorient link command"); //$NON-NLS-1$
        }
        if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
            return reorientSource();
        }
        if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
            return reorientTarget();
        }
        throw new IllegalStateException();
    }

    /**
     * @generated
     */
    protected CommandResult reorientSource() throws ExecutionException {
        getOldSource().getParentCompleteComponentTypes().remove(getOldTarget());
        getNewSource().getParentCompleteComponentTypes().add(getOldTarget());
        return CommandResult.newOKCommandResult(referenceOwner);
    }

    /**
     * @generated
     */
    protected CommandResult reorientTarget() throws ExecutionException {
        getOldSource().getParentCompleteComponentTypes().remove(getOldTarget());
        getOldSource().getParentCompleteComponentTypes().add(getNewTarget());
        return CommandResult.newOKCommandResult(referenceOwner);
    }

    /**
     * @generated
     */
    protected ImplementationComponentType getOldSource() {
        return (ImplementationComponentType) referenceOwner;
    }

    /**
     * @generated
     */
    protected ImplementationComponentType getNewSource() {
        return (ImplementationComponentType) newEnd;
    }

    /**
     * @generated
     */
    protected CompleteComponentType getOldTarget() {
        return (CompleteComponentType) oldEnd;
    }

    /**
     * @generated
     */
    protected CompleteComponentType getNewTarget() {
        return (CompleteComponentType) newEnd;
    }
}
