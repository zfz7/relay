package org.zfz7.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.zfz7.domain.Peer

@Repository
interface PeerRepository : JpaRepository<Peer, Long>