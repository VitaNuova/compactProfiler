/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */
package cz.muni.fi.sbapr;

import cz.muni.fi.sbapr.analyzer.CompactProfileAnalyzerImpl;
import cz.muni.fi.sbapr.analyzer.result.AnalysisResult;
import cz.muni.fi.sbapr.analyzer.util.IOUtils;
import cz.muni.fi.sbapr.enums.CompactProfile;
import cz.muni.fi.sbapr.enums.ResolutionType;
import cz.muni.fi.sbapr.exceptions.AnalyzerException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Path("jarupload")
public class ProfilerResource {

    /**
     * REST resource class that allows to submit user request for jar archive analysis and return the response
     * @param input form data containing jar archive and desired compact profile
     * @return response containing minimal compact profile and lists of packages within and outside the desired compact profile,
     *          or error code in case analysis failed
     */
    @POST
    @Consumes("multipart/form-data")
    @Produces("application/json")
    public Response uploadJar(MultipartFormDataInput input) {

        AnalysisResult result;
        try {
            InputStream archive = input.getFormDataPart("fileField", InputStream.class, null);
            String profile = input.getFormDataPart("profileField", String.class, null);
            File pathToArchive = IOUtils.readFromJarStream(archive);
            switch(profile) {
                case "Compact 1": result = CompactProfileAnalyzerImpl.ANALYZER.analyze(pathToArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);
                                  break;
                case "Compact 2": result = CompactProfileAnalyzerImpl.ANALYZER.analyze(pathToArchive, ResolutionType.MAVEN, CompactProfile.COMPACT2);
                                  break;
                case "Compact 3": result = CompactProfileAnalyzerImpl.ANALYZER.analyze(pathToArchive, ResolutionType.MAVEN, CompactProfile.COMPACT3);
                                  break;
                case "Full SE API": result = CompactProfileAnalyzerImpl.ANALYZER.analyze(pathToArchive, ResolutionType.MAVEN, CompactProfile.FULL_JRE);
                                  break;
                default:          result = CompactProfileAnalyzerImpl.ANALYZER.analyze(pathToArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);

            }

        }
        catch(IOException ex) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        catch(AnalyzerException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Status.OK).entity(result).build();
    }

}
