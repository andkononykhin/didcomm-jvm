package org.dif.crypto.key

import org.dif.diddoc.DIDDocResolver
import org.dif.exceptions.DIDDocException
import org.dif.exceptions.DIDDocNotResolvedException
import org.dif.secret.SecretResolver
import org.dif.utils.divideDIDFragment
import org.dif.utils.isDIDFragment

class RecipientKeySelector(private val didDocResolver: DIDDocResolver, private val secretResolver: SecretResolver) {
    fun verifyKey(selector: String): Key = Key.wrapVerificationMethod(
        let {
            val (did) = divideDIDFragment(selector)
            val didDoc = didDocResolver.resolve(did).orElseThrow { throw DIDDocNotResolvedException(did) }

            val verificationMethodId = if (isDIDFragment(selector)) { selector } else {
                didDoc.authentications.firstOrNull()
                    ?: throw DIDDocException("Authentication is not found in DID Doc '$did'")
            }

            didDoc.findVerificationMethod(verificationMethodId)
        }
    )
}