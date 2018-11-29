package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.ReadRepository;

public interface AuthRequestRepository extends ReadRepository<AuthRequest, String>
{

    /**
     * Creates a new request for authentication.
     *
     * @param service  The service requesting authentication.
     * @param callback The callback to post to when the user authenticates.
     * @param template An optional permission template that should be accepted when the user authenticates.
     *                 Can be {@code null}.
     * @return The newly created authentication request.
     */
    AuthRequest create(Service service, String callback, PermissionTemplate template);

    /**
     * Marks the provided authentication request {@link AuthRequest.Status#COMPLETED}.
     *
     * @param authRequest The authentication request to mark completed.
     */
    void completed(AuthRequest authRequest);
}
