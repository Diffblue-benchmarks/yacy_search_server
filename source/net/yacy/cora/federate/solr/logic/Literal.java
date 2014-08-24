/**
 *  Literal
 *  Copyright 2014 by Michael Peter Christen
 *  First released 03.08.2014 at http://yacy.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program in the file lgpl21.txt
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package net.yacy.cora.federate.solr.logic;

import org.apache.solr.common.SolrDocument;

import net.yacy.cora.federate.solr.SchemaDeclaration;
import net.yacy.cora.federate.solr.connector.AbstractSolrConnector;

public class Literal extends AbstractTerm implements Term {

    private SchemaDeclaration key;
    private String value;
    
    public Literal(final SchemaDeclaration key, final String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Object clone() {
        return new Literal(this.key, this.value);
    }

    @Override
    public boolean equals(Object otherTerm) {
        if (!(otherTerm instanceof Literal)) return false;
        Literal o = (Literal) otherTerm;
        return this.key.equals(o.key) && this.value.equals(o.value);
    }
    
    @Override
    public int hashCode() {
        return key.hashCode() + value.hashCode();
    }
    
    /**
     * the length attribute of a term shows if rewritten terms
     * (using rules of replacement as allowed for propositional logic)
     * are shorter and therefore more efficient.
     * @return the number of operators plus the number of operands plus one
     */
    @Override
    public int weight() {
        return 1;
    }
    
    /**
     * create a Solr query string from this literal
     * @return a string which is a Solr query string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.key.getSolrFieldName());
        sb.append(':').append('"').append(this.value).append('"');
        return sb.toString();
    }
    
    /**
     * check if the key/value pair of this literal occurs in the SolrDocument
     * @param doc the document to match to this literal
     * @return true, if the key of this literal is contained in the document and the
     *   value equals (does not equal) with the value if this literal (if the signature is false)
     */
    @Override
    public boolean matches(SolrDocument doc) {
        Object v = doc.getFieldValue(this.key.getSolrFieldName());
        if (v == null) return false;
        return this.value.equals(AbstractSolrConnector.CATCHALL_TERM) || v.toString().matches(this.value);
    }
    
    @Override
    public Term lightestRewrite() {
        return this;
    }
}